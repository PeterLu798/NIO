package com.io.channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author lubaijiang
 * 锁
 */
public class FileChannelMain2 {
    /**
     * lock(long position, long size, boolean shared)
     * (1) 该方法具有同步性
     * (2) 当该方法在执行期间（注意是执行期间），如果另外一个线程调用了close方法，则该方法会抛出AsynchronousCloseException异常
     * (3) 当线程被interrupt之后在调用lock方法会抛出 FileLockInterruptionException
     * (4) 如果线程已获得了锁，此时被interrupt之后，方法也会抛出 FileLockInterruptionException
     * <p>
     * lock()
     * (1) 其源码实现是：return lock(0, Long.MAX_VALUE, false);
     * <p>
     * tryLock(long position, long size, boolean shared)
     * 与lock方法的唯一区别是：该方法是非阻塞的，也就是说当该方法拿不到锁时，返回null对象，而不是一直阻塞在那
     * <p>
     * tryLock()
     * 其源码实现是：return tryLock(0, Long.MAX_VALUE, false);
     */
    public static void main1(String[] args) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\tmp\\a.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 1000000; i++) {
                        System.out.println("i=" + (i + 1));
                    }
                    FileLock lock = channel.lock(1, 2, false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();
        Thread.sleep(50);
        a.interrupt();
        Thread.sleep(30000);
    }

    public static void main2(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\tmp\\a.txt", "rw");
                    FileChannel channel = randomAccessFile.getChannel();
                    System.out.println("B start");
                    FileLock lock = channel.lock(1, 2, false);
                    System.out.println("B end");
                    lock.release();
                    channel.close();
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }

    /**
     * (1) 共享锁自己不能写
     * (2) 共享锁别人也不能写
     * (3) 共享锁自己能读
     * (4) 共享锁别人也能读
     * -------------------------------
     * (1)(2)(3)(4)总结为共享锁是只读的
     * <p>
     * (5) 独占锁自己能写
     * (6) 独占锁别人不能写
     * (7) 独占锁可以自己读
     * (8) 独占锁别人不能读
     * -------------------------------
     * (5)(6)(7)(8)总结为独占锁只有自己可以读写，其他人不允许读写
     * <p>
     * (9) 文件未锁定的部分自己（别人）可以读写
     */
    public static void main3(String[] args) throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\tmp\\b.txt", "rw");
                    FileChannel channel = randomAccessFile.getChannel();
                    System.out.println("A start");
                    FileLock lock = channel.lock(1, 2, false);
                    System.out.println("A end");
                    Thread.sleep(Integer.MAX_VALUE);
                    channel.close();
                    randomAccessFile.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RandomAccessFile file = new RandomAccessFile("E:\\tmp\\b.txt", "rw");
                    FileChannel channel = file.getChannel();
//                    channel.write(ByteBuffer.wrap("123456".getBytes()));
                    channel.read(ByteBuffer.allocate(10));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(10);
        thread1.start();
    }

    public static void main4(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("E:\\tmp\\a.txt", "rw");
        FileChannel channel = file.getChannel();
        channel.lock(1, 2, false);
        channel.read(ByteBuffer.allocate(10));
    }

    public static void main5(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("E:\\tmp\\a.txt", "rw");
        FileChannel channel = file.getChannel();
        channel.lock(3, 2, true);
    }

    /**
     * (1) 共享锁与共享锁之间不互斥
     * (2) 共享锁与独占锁之间是互斥的：第二个线程是阻塞的
     * (3) 独占锁与共享锁之间是互斥的：第二个线程是阻塞的
     * (4) 独占锁与独占锁之间是互斥的：第二个线程是阻塞的
     */
    public static void main6(String[] args) throws IOException, InterruptedException {
        RandomAccessFile file = new RandomAccessFile("E:\\tmp\\a.txt", "rw");
        FileChannel channel = file.getChannel();
        channel.tryLock(0, Long.MAX_VALUE, true);
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * FileLock的其他方法
     * 释放锁的方法：
     * release()
     * close()
     * 判断锁是否共享：
     * isShared()
     * 判断锁是否有效：
     * isValid()
     * 获取锁的所在通道：
     * acquiredBy()
     * 探测锁覆盖的区域：
     * boolean overlaps(long position, long size)
     *
     *
     */
    public static void main(String[] args) throws IOException {
        File file = new File("E:\\tmp\\a.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        System.out.println("channel.hashCode(): " + channel.hashCode());
        //上锁
        FileLock lock = channel.lock(1, 10, true);
        System.out.println("A lock.position: " + lock.position() + " size: " + lock.size() + " isShared: " + lock.isShared() + " isValid: " + lock.isValid() + " hashCode: " + lock.hashCode() + " acquiredBy().hashCode(): " + lock.acquiredBy().hashCode());
        //释放锁
        lock.release();
        //上独占锁
        lock = channel.lock(1, 10, false);
        System.out.println("B lock.position: " + lock.position() + " size: " + lock.size() + " isShared: " + lock.isShared() + " isValid: " + lock.isValid() + " hashCode: " + lock.hashCode() + " acquiredBy().hashCode(): " + lock.acquiredBy().hashCode());
        //释放锁
        lock.close();

        channel.close();
        System.out.println("C lock.position: " + lock.position() + " size: " + lock.size() + " isShared: " + lock.isShared() + " isValid: " + lock.isValid() + " hashCode: " + lock.hashCode() + " acquiredBy().hashCode(): " + lock.acquiredBy().hashCode());

    }
}
