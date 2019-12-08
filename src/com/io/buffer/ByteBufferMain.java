package com.io.buffer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * @author lubaijiang
 */
public class ByteBufferMain {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
//        System.out.println(byteBuffer.capacity());
//        System.out.println(byteBuffer.limit());
//        byteBuffer.put((byte) 8);
//        System.out.println(byteBuffer.position());
//        System.out.println(byteBuffer.remaining());
//
//        System.out.println();
//
        byte[] bytes = {1,2,3,4,5};
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(bytes);
//        System.out.println(byteBuffer1.capacity());
//        System.out.println(byteBuffer1.limit());
//        System.out.println(byteBuffer1.position());
//        System.out.println(byteBuffer1.remaining());
//
//        System.out.println();
//
//        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(10);
//        System.out.println(byteBuffer2.capacity());
//        System.out.println(byteBuffer2.limit());
//        System.out.println(byteBuffer2.position());
//        System.out.println(byteBuffer2.remaining());
//
//        byte b = byteBuffer2.get();
//        byteBuffer2.get(new byte[10]);
//        System.out.println(Charset.defaultCharset().name());
//        ByteBuffer byteBuffer3 = ByteBuffer.wrap(new byte[]{1, 2, 3, 4});
//        byteBuffer3.position(2);
//        byteBuffer3.compact();
//        System.out.println(byteBuffer3.capacity());
//        System.out.println(byteBuffer3.position());
//        System.out.println(byteBuffer3.limit());
//        System.out.println(new String(byteBuffer3.array(), Charset.forName("utf-16BE")));


//        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
//        buffer.position(2);
//        ByteBuffer buffer1 = buffer.slice();
//
//        byte[] array = buffer.array();
//        byte[] array1 = buffer1.array();

//        for(int i=0; i<array.length; i++){
//            System.out.print(array[i]);
//        }
//
//        System.out.println();
//
//        for(int i=0; i<array1.length; i++){
//            System.out.print(array1[i]);
//        }

//        System.out.println(buffer.arrayOffset());
//        System.out.println(buffer1.arrayOffset());

        System.out.println(byteBuffer.compareTo(byteBuffer1));

    }
    /**
     * 直接缓冲区的内存回收-手动回收
     *
     * @param args
     * @throws InterruptedException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void main1(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
        byte[] bytes = new byte[]{1};
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            byteBuffer.put(bytes);
        }
        Thread.sleep(1000);
        Method cleaner = byteBuffer.getClass().getMethod("cleaner");
        cleaner.setAccessible(true);
        Object invoke = cleaner.invoke(byteBuffer);
        Method clear = invoke.getClass().getMethod("clean");
        clear.setAccessible(true);
        clear.invoke(invoke);
        System.out.println("回收完毕");
    }

    /**
     * 直接缓冲区与非直接缓存区的性能比较
     *
     * @param args
     */
    public static void main2(String[] args) {
        //直接缓冲区
        int capacity = Integer.MAX_VALUE;
        long start = System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
        for (int i = 0; i < capacity; i++) {
            byteBuffer.put((byte) 123);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - start);

    }

    public static void main3(String[] args) {
        //非直接缓冲区
        int capacity = Integer.MAX_VALUE;//设置为最大时OOM了
        long start = System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        for (int i = 0; i < capacity; i++) {
            byteBuffer.put((byte) 123);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - start);
    }
    /**
     * 通过以上实验证明使用直接缓存的速度并不一定比非直接缓存的快，但是直接缓存的空间大小上线取决于物理内存，而非直接缓存取决于JVM
     */

    /**
     * wrap(byte[], offset, length)
     *
     * @param args
     */
    public static void main4(String[] args) {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 1, 2);
        System.out.println(byteBuffer.capacity() + " " + byteBuffer.limit() + " " + byteBuffer.position());
        byteBuffer.put(2, (byte) 5);
    }

    public static void main5(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        System.out.println(byteBuffer.capacity() + " " + byteBuffer.limit() + " " + byteBuffer.position() + " " + byteBuffer.arrayOffset());
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        byteBuffer.put(bytes, 0, 4);
        System.out.println(byteBuffer.capacity() + " " + byteBuffer.limit() + " " + byteBuffer.position() + " " + byteBuffer.arrayOffset());

        byteBuffer.rewind();
        byte[] bytes1 = new byte[5];
        byteBuffer.get(bytes1, 0, 5);
        for (int i = 0; i < bytes1.length; i++) {
            System.out.println(bytes1[i]);
        }
    }

    /**
     * put(byte[] src, int offset, int length)
     * get(byte[] dst, int offset, int length)
     * @param args
     */
    public static void main6(String[] args) {
        byte[] bytes1 = new byte[]{1,2,3,4,5,6,7,8};
        byte[] bytes2 = {55, 66, 77, 88};
        //开辟10个空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        //将bytes1放进缓冲区的前8个位置
        byteBuffer.put(bytes1);
        //将位置设置为2
        byteBuffer.position(2);
        byteBuffer.put(bytes2, 1, 3);
        byte[] array = byteBuffer.array();
        for(int i=0; i< array.length; i++){
            System.out.println(array[i] + " ");
        }

        System.out.println("-------------------------------------");
        byteBuffer.position(1);
        //创建新的数组
        byte[] dst = new byte[byteBuffer.capacity()];
        //get()
        byteBuffer.get(dst, 3, 4);
        for(int i=0; i<dst.length; i++){
            System.out.println(dst[i] + " ");
        }
    }

    /**
     * get(byte[] dst, int offset, int length)
     * 异常情况
     * @param args
     */
    public static void main7(String[] args) {
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7};
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        //IndexOutOfBoundsException
//        byteBuffer.put(bytes, 0, byteBuffer.capacity());
        //BufferOverflowException
        byteBuffer.position(9);
        byteBuffer.put(bytes, 0, 4);
    }
    public static void main8(String[] args) {
        byte[] bytes = {1,2,3,4,5,6,7,8,9,10,11,12};
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        int index = 0;
        while(index < bytes.length){
            int readLength = Math.min(byteBuffer.remaining(), bytes.length - index);
            byteBuffer.put(bytes, index, readLength);
        }
    }

    /**
     * ！！！注意：get(byte[] dst) 方法 dst的长度不能超过remaining()
     * @param args
     */
    public static void main9(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        System.out.println(byteBuffer.position() + " " + byteBuffer.limit() + " " + byteBuffer.remaining());
        byte[] array = new byte[3];
        byteBuffer.get(array);
        for(int i=0; i<array.length; i++){
            System.out.println(array[i]);
        }
        //下面会异常

        ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[]{1, 2, 3});
        System.out.println(byteBuffer2.position() + " " + byteBuffer2.limit() + " " + byteBuffer2.remaining());
        byte[] array2 = new byte[10];
        byteBuffer2.get(array2);

    }

    /**
     * 相对方法
     * put(byte b)、put(byte[] src, int offset, int length)、put(byte[] src) 核心要点：1.都是从position开始 2. length不能大于remaining()
     * get()、get(byte[] dst, int offset, int length)、get(byte[] dst) 核心要点在：1.都是从position开始 2.length不能大于remaining()
     * put(ByteBuffer src) 将src中的remaining()字节传输到此缓冲区的当前位置。核心要点：1.如果源缓冲区中的remain() > 此缓冲区.remaining() 则抛异常 2.两个缓冲区position都增加 n= src.remaining()
     */
    /**
     * 绝对方法
     * put(int index, byte b)   get(int index) 核心要点：position不变
     */
    /**
     * 给字节缓冲区添加其他数据类型
     * putChar(char value)    putChar(int index, char value)    getChar()    getChar(int index)
     * putInt(int value)      putInt(int index, int value)      getInt()     getInt(int index)
     * putLong(long value)    putLong(int index, long value)    getLong()    getLong(int index)
     * putFloat(float value)  putFloat(int index, float value)  getFloat()   getFloat(int index)
     * putDouble(double value) putDouble(int index, double value) getDouble() getDouble(int index)
     */
    //练习题
    public static void main10(String[] args) {
        //问题一：put(byte b) position会加1 那么put(byte[] src,int offset, int length) position会加几？
        //回答：会增加 length
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.position(1);
        System.out.println(byteBuffer.position() + " " + byteBuffer.limit());
        byteBuffer.put(new byte[]{1,2,3,4,5,6}, 2, 4);
        System.out.println(byteBuffer.position() + " " + byteBuffer.limit());

        //问题二：get() 会让position增加1，那么get(byte[] dst, int offset, int length) 会让position增加几？
        //回答：length
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{1,2,3,4,5,6});
        System.out.println(byteBuffer1.position() + " " + byteBuffer1.limit());
        byte[] dst = new byte[5];
        byteBuffer1.get(dst, 1, 4);
        System.out.println(byteBuffer1.position() + " " + byteBuffer1.limit());
    }


}
