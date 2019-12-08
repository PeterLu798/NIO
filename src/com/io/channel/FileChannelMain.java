package com.io.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lubaijiang
 */
public class FileChannelMain {
    /**
     * FileChannel的特性：
     * (1) 提供对文件绝对位置的字节进行读写操作，从而不影响通道当前位置
     * (2) 将文件中的某个区域直接映射到内存中
     * (3) 强制对底层的存储设备进行文件的更新，确保数据安全
     * (4) 优化传输方式，能高速将字节从文件传输到通道，或从通道传输到文件
     * (5) 可以锁定某个文件区域，以阻止其他程序对其进行访问
     * (6) FileChannel的定义为一个可以读取、写入、操作文件的阻塞通道，那么既然是阻塞的为何是NIO技术呢？
     * 答案：一、对于涉及更改通道位置或更改文件大小的操作，该通道是阻塞的；二、对于其他操作，特别是那些采用显示位置的操作，是可以并发处理的
     * 2019/11/27 上述答案错误，这里还没应用多路复用技术，因此不算是真正的NIO
     */

    public static void main1(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\tmp\\a.txt"));
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{'a', 'b', 'c', 'd', 'e'});
        try {
            /**
             * write(ByteBuffer src) 方法会使channel和buffer的position自增，增加的数值为buffer.remaining()
             */
            System.out.println("position1: " + channel.position());
            int write = channel.write(byteBuffer);
            System.out.println("返回值：" + write);
            System.out.println("position2: " + channel.position());

            int write1 = channel.write(byteBuffer);
            System.out.println("write1= " + write1);
            System.out.println("position3: " + channel.position());

            byteBuffer.rewind();
            int write2 = channel.write(byteBuffer);
            System.out.println("write2= " + write2);
            System.out.println("position3：" + channel.position());

            /**
             * write(ByteBuffer src) 方法写的是buffer remaining之间的元素
             */
            ByteBuffer byteBuffer1 = ByteBuffer.wrap("123456".getBytes());
            byteBuffer1.position(3);
            int write3 = channel.write(byteBuffer1);
            System.out.println("write3= " + write3);
            System.out.println("position4: " + channel.position());

            /**
             * write(ByteBuffer src) 方法具有同步性
             */
            synchroniseWrite();


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            channel.close();
            fileOutputStream.close();
        }

    }

    public static void synchroniseWrite() throws IOException, InterruptedException {
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\b.txt"));
        FileChannel channel = outputStream.getChannel();
        for (int i = 0; i < 10; i++) {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer byteBuffer = ByteBuffer.wrap("1234\r\n".getBytes());
                    try {
                        channel.write(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer byteBuffer = ByteBuffer.wrap("abcd\r\n".getBytes());
                    try {
                        channel.write(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread1.start();
            thread2.start();
        }

        Thread.sleep(3000);
        channel.close();
        outputStream.close();
    }

    public static void main2(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("E:\\tmp\\c.txt"));
        FileChannel channel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        try {
            /**
             * read(ByteBuffer src)返回值的含义：
             * 大于0：读取的字节数
             * 等于0：可读取的字节数为0 / 缓冲区没空间了
             * 小于0：到达了文件末尾
             */
            int read = channel.read(byteBuffer);
            System.out.println("read= " + read);
            //缓冲区没空间了
            read = channel.read(byteBuffer);
            System.out.println("read2= " + read);
            byteBuffer.clear();
            read = channel.read(byteBuffer);
            System.out.println("read3= " + read);
            //到达文件末尾了
            byteBuffer.clear();
            read = channel.read(byteBuffer);
            System.out.println("read4= " + read);
            /**
             * read(ByteBuffer src)
             * (1) 是从通道的position开始读的
             * (2) 将读取的字节从src的当前位置放入
             */
            /**
             * read(ByteBuffer src) 方法具有同步性
             */
            synchroniseRead();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            channel.close();
            inputStream.close();
        }
    }

    public static void synchroniseRead() throws IOException, InterruptedException {
        FileInputStream outputStream = new FileInputStream(new File("E:\\tmp\\b.txt"));
        FileChannel channel = outputStream.getChannel();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                try {
                    int read = channel.read(byteBuffer);
                    while (read != -1){
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.clear();
                        read = channel.read(byteBuffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                try {
                    int read = channel.read(byteBuffer);
                    while (read != -1){
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.clear();
                        read = channel.read(byteBuffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        thread1.start();

        Thread.sleep(3000);
        channel.close();
        outputStream.close();
    }

    /**
     * write(ByteBuffer[] srcs) 批量写
     * (1) 该方法作用是将每个缓冲区的remaining字节写入通道当前位置
     * (2) 此方法等同于write(srcs, 0, srcs.length)
     * (3) 此方法是同步的
     * (4) 该方法实现的是GatheringByChannel接口中的同名方法；而GatheringByChannel的父接口是WritableByteChannel
     * @param args
     */
    public static void main3(String[] args) throws IOException, InterruptedException {
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\d.txt"));
        FileChannel channel = outputStream.getChannel();
        channel.write(ByteBuffer.wrap("abc".getBytes()));
        System.out.println("channel.position= " + channel.position());

        ByteBuffer byteBuffer = ByteBuffer.wrap("00001".getBytes());
        ByteBuffer byteBuffer1 = ByteBuffer.wrap("00002".getBytes());
        channel.write(new ByteBuffer[]{byteBuffer, byteBuffer1});
        System.out.println("channel.position1= " + channel.position());

        //批量写具有同步性
        synchroniseWrites();

        channel.close();
        outputStream.close();
    }

    public static void synchroniseWrites() throws IOException, InterruptedException {
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\e.txt"));
        FileChannel channel = outputStream.getChannel();
        for(int i=0; i<10; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer byteBuffer = ByteBuffer.wrap("00001\r\n".getBytes());
                    ByteBuffer byteBuffer1 = ByteBuffer.wrap("00002\r\n".getBytes());
                    try {
                        channel.write(new ByteBuffer[]{byteBuffer, byteBuffer1});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer byteBuffer = ByteBuffer.wrap("00003\r\n".getBytes());
                    ByteBuffer byteBuffer1 = ByteBuffer.wrap("00004\r\n".getBytes());
                    try {
                        channel.write(new ByteBuffer[]{byteBuffer, byteBuffer1});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            thread1.start();
        }
        Thread.sleep(3000);
        channel.close();
        outputStream.close();
    }

    /**
     * long read(ByteBuffer[] dsts)
     * (1) 此方法的作用是将通道中当前位置的字节依次读入到各个缓冲区的当前位置
     * (2) read方法会使通道的position自增，也会使当前正在读的缓冲区的position自增
     * (3) 此方法具有同步性
     * (4) 此方法是实现ScatteringByteChannel接口中的同名方法，而此接口继承了ReadableByteChannel，因此它具有ReadableByteChannel的特性
     * (5) 返回值的意义：
     * 大于0：表示几个缓冲区读取的字节的总数
     * 等于0：缓冲区的remaining的和为0
     * -1：达到文件末尾，并且这时各个缓冲区读取的字节数都为0
     * (6) 此方法完全等同于 c.read(dsts, 0, dsts.length)
     * @param args
     */
    public static void main0(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("E:\\tmp\\d.txt"));
        FileChannel channel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(5);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(5);
        long read = channel.read(new ByteBuffer[]{byteBuffer, byteBuffer1, byteBuffer2});
        System.out.println("read= " + read);
        System.out.println("byteBuffer: " + new String(byteBuffer.array()));
        System.out.println("byteBuffer1: " + new String(byteBuffer1.array()));
        System.out.println("byteBuffer2: " + new String(byteBuffer2.array()));

        read = channel.read(new ByteBuffer[]{byteBuffer, byteBuffer1, byteBuffer2});
        System.out.println("read1= " + read);
    }

    /**
     * write(ByteBuffer[] srcs, int offset, int length)
     * (1) 此方法的作用：从srcs下标为offset的元素开始，取length个元素，将这些元素依次写入文件中
     * (2) 写入通道是从当前位置开始
     * (3) 取的是每个缓冲区中remaining区间内的字节
     * (4) 该方法会使通道的position自增，会使srcs中offset到 length-offset的缓冲区的position自增
     * (5) 该方法是同步的
     */
    public static void main5(String[] args) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\f.txt"));
        ByteBuffer byteBuffer = ByteBuffer.wrap("abcde".getBytes());
        ByteBuffer byteBuffer1 = ByteBuffer.wrap("12345".getBytes());
        ByteBuffer byteBuffer2 = ByteBuffer.wrap("67890".getBytes());
        ByteBuffer[] byteBuffers = {byteBuffer, byteBuffer1, byteBuffer2};
        FileChannel channel = outputStream.getChannel();
        channel.write(byteBuffers, 1, 2);
        System.out.println("channel.position= " + channel.position());
        System.out.println("buffer.position= " + byteBuffer.position());
        System.out.println("buffer1.position= " + byteBuffer1.position());
        System.out.println("buffer2.position= " + byteBuffer2.position());
    }

    /**
     * long read(ByteBuffer[] dsts, int offset, int length)
     * (1) 该方法的作用是读取通道中的字节依次写入dsts中的offset 到 (length-offset) 的缓冲区中
     * (2) 该方法是从通道的当前位置开始读取
     * (3) 读入缓冲区中的位置是 remaining之间
     * (4) 该方法是同步的
     * (5) 该方法返回值的含义：
     * 大于0：读入到缓冲区中的元素的个数
     * 等于0：offset到length-offset之间的缓冲区的remaining都为0
     * -1：达到文件末尾，并且这时各个缓冲区读取的字节数都为0
     * (6) 该方法会使通道的position自增，也会使当前正在读的缓冲区的position自增
     */
    public static void main6(String[] args) throws IOException, InterruptedException {
        //验证方法是同步的
        FileInputStream inputStream = new FileInputStream(new File("E:\\tmp\\e.txt"));
        FileChannel channel = inputStream.getChannel();
        for(int i=0; i< 10; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer[] byteBuffers = {ByteBuffer.allocate(7), ByteBuffer.allocate(7)};
                    try {
                        long read = channel.read(byteBuffers, 0, 2);
                        while (read != -1){
                            synchronized (FileChannelMain.class){
                                for(int j = 0; j < byteBuffers.length; j++){
                                    System.out.print(new String(byteBuffers[j].array()));
                                    byteBuffers[j].clear();
                                }
                            }
                            read = channel.read(byteBuffers, 0, 2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(thread.getName());
            thread.start();
        }
        Thread.sleep(3000);
        channel.close();
        inputStream.close();
    }

    /**
     * write(ByteBuffer src, long position)
     * (1) 将缓冲区remaining写入到通道指定的位置
     * (2) 方法具有同步性
     * (3) 该方法会使缓冲区的position增加，但不会更改通道的position
     *
     * read(ByteBuffer dst, long position)
     * (1) 将通道中指定位置的字节序列读入到缓冲区的当前位置
     * (2) 该方法会使缓冲区的position增加，但不会更改通道的position
     * (3) 返回值的含义：
     * 大于0：表示读出的字节数
     * 等于0：表示缓冲区remaining为0
     * -1：到达文件末尾
     * (4) 该方法具有同步性
     */
    public static void maing(String[] args) throws IOException, InterruptedException {
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\g.txt"));
        FileChannel channel = outputStream.getChannel();
        System.out.println("channel: "+channel.position());
        ByteBuffer byteBuffer = ByteBuffer.wrap("abc".getBytes());
        System.out.println("byteBuffer: " + byteBuffer.position());
        channel.write(byteBuffer, 6);
        System.out.println("channel1: "+channel.position());
        System.out.println("byteBuffer1: " + byteBuffer.position());
//
//        channel.close();
//        outputStream.close();
//
//        FileInputStream inputStream = new FileInputStream(new File("E:\\tmp\\h.txt"));
//        FileChannel channel1 = inputStream.getChannel();
//        ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
////        byteBuffer1.position(1);
//        int read = channel1.read(byteBuffer1, 6);
//        System.out.println("read: " + read);
//        System.out.println("byteBuffer1.position: " + byteBuffer1.position());
//        System.out.println("channel1.position: " + channel1.position());
//        System.out.println(new String(byteBuffer1.array()));
        //验证这两个方法具有同步性
//        long start = System.currentTimeMillis();
//        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("E:\\tmp\\CentOS-7-x86_64-DVD-1810.iso"), "rw");
//        FileChannel channel = randomAccessFile.getChannel();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024 * 1024 * 5);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    channel.read(byteBuffer, 0);
//                    System.out.println("end thread1 " + System.currentTimeMillis());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    channel.write(ByteBuffer.wrap(new byte[]{1,2,3,4}), channel.size() +1);
//                    System.out.println("end thread2 " + System.currentTimeMillis());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        System.out.println("start time: " + System.currentTimeMillis());
//        thread.start();
//        Thread.sleep(100);
//        thread1.start();

//        channel.read(byteBuffer, 0);
//        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\copy.ios"));
//        FileChannel channel1 = outputStream.getChannel();
//        byteBuffer.rewind();
//        channel1.write(byteBuffer);
//
//        channel1.close();
//        outputStream.close();
//        channel.close();
//        randomAccessFile.close();
//
//        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * channel.truncate(long size)
     * (1)此方法的作用为截取
     * (2)截取后通道的大小为：min(size, channel.size())
     * (3)从0位置开始截取
     */
    public static void main8(String[] args) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\newFile.txt"));
        FileChannel channel = outputStream.getChannel();
        channel.write(ByteBuffer.wrap("123456789".getBytes()));

        System.out.println(channel.position() + " " + channel.size());
        FileChannel channel1 = channel.truncate(4);
        System.out.println(channel1.position() + " " + channel1.size());
        System.out.println(channel.position() + " " + channel.size());
    }

    /**
     * inputStreamChannel.transferTo(long position, long count, outputStreamChannel);
     * 从input的position处开始，复制count个字节，然后放入到output的当前位置
     * (1) 该方法不会使input的position自增，但会使output的position自增
     * (2) 返回值表示实际传输的字节数：
     * 当position大于等于input的size时，实际传输的字节数为0
     * 当position小于input的size时，实际传输的字节数为 Math.min(count, inputStreamChannel.size()) - position
     *
     *
     *
     */
    public static void maind(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("E:\\tmp\\f.txt", "rw");
        FileChannel channel = file.getChannel();
        System.out.println("this初始position: " + channel.position());

        RandomAccessFile file1 = new RandomAccessFile("E:\\tmp\\ff.txt", "rw");
        FileChannel channel1 = file1.getChannel();
        System.out.println("target初始position: " + channel1.position());

        long l = channel.transferTo(2, 3, channel1);
        System.out.println("this写之后position: " + channel.position());
        System.out.println("target写之后position: " + channel1.position());
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("E:\\tmp\\ff.txt", "rw");
        FileChannel channel = file.getChannel();
        System.out.println("this初始position: " + channel.position());

        RandomAccessFile file1 = new RandomAccessFile("E:\\tmp\\f.txt", "rw");
        FileChannel channel1 = file1.getChannel();
        System.out.println("src初始position: " + channel1.position());

        long l = channel.transferFrom(channel1, 1, 3);
        System.out.println("this读之后position: " + channel.position());
        System.out.println("src读之后position: " + channel1.position());
    }


}
