package com.io.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

/**
 * @author lubaijiang
 */
public class FileChannelMain3 {
    public static void main1(String[] args) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\tmp\\b.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        System.out.println("A start");
        channel.lock(1, 2, false);
        System.out.println("A end");
        Thread.sleep(Integer.MAX_VALUE);
        channel.close();
        randomAccessFile.close();
    }

    public static void main2(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("E:\\tmp\\a.txt", "rw");
        FileChannel channel = file.getChannel();
        System.out.println("----------start---------");
        FileLock fileLock = channel.tryLock(0, Long.MAX_VALUE, false);
        if (null != fileLock) {
            System.out.println("end 拿到锁了");
        }else {
            System.out.println("end 未拿到锁");
        }
    }

    /**
     * force(boolean metaData) 方法的目的是尽最大的努力减少数据的丢失
     * 下列实验演示了force对系统性能的极大影响：
     * 当注释掉 outputStreamChannel.force(false);这一行时速度明显快了很多
     * 参数的含义：是否更新元数据（什么是元数据，就比如说有的操作系统将文件访问时间也作为元数据），这个参数的实现取决于操作系统，如Linux系统不管你传true/false都会更新元数据，因为其底层调用的都是fsync()方法
     */
    public static void main11(String[] args) throws IOException {
        File file = new File("E:\\tmp\\a.txt");
        if(!file.exists()){
            file.createNewFile();
        }else {
            file.delete();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        FileChannel outputStreamChannel = outputStream.getChannel();
        long startTime = System.currentTimeMillis();
        for(int i=0; i<5000; i++){
            outputStreamChannel.write(ByteBuffer.wrap("abcde".getBytes()));
            outputStreamChannel.force(false);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        outputStreamChannel.close();
    }

    public static void main12(String[] args) throws IOException {
        File file = new File("E:\\tmp\\a.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        channel.write(ByteBuffer.wrap("abcdefg".getBytes()), 0);
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 6);
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get());
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get());
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get());
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get());
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get());
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get());

        buffer = channel.map(FileChannel.MapMode.READ_WRITE, 6, 3);
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get() + " after position=" + buffer.position());
        System.out.println("position=" + buffer.position() + " " + (char) buffer.get() + " after position=" + buffer.position());
//        System.out.println("position=" + buffer.position() + " " + (char) buffer.get() + " after position=" + buffer.position());

        buffer.put((byte) 'c');

        buffer = channel.map(FileChannel.MapMode.PRIVATE, 0, 4);
        buffer.put(ByteBuffer.wrap("xyz".getBytes()));
        buffer.force();
        buffer.position(0);
        System.out.println(buffer.get() + " position:" + buffer.position());
        System.out.println(buffer.get() + " position:" + buffer.position());
        System.out.println(buffer.get() + " position:" + buffer.position());
        System.out.println(buffer.get() + " position:" + buffer.position());
    }

    public static void main13(String[] args) throws IOException {
        File file = new File("E:\\tmp\\a.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.PRIVATE, 0, Integer.MAX_VALUE);

        System.out.println(map.isLoaded());

        map.load();

        System.out.println(map.isLoaded());

        channel.close();
        randomAccessFile.close();
    }

    public static void main(String[] args) throws IOException {
        FileChannel channel = FileChannel.open(new File("E:\\tmp\\aaa.txt").toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE);
//        channel.write(ByteBuffer.wrap("abcdefg".getBytes()));

        MappedByteBuffer map = channel.map(FileChannel.MapMode.PRIVATE, 0, channel.size());
        MappedByteBuffer load = map.load();

        channel.close();

    }


}
