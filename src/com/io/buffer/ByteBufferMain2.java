package com.io.buffer;

import java.nio.ByteBuffer;

/**
 * @author lubaijiang
 */
public class ByteBufferMain2 {

    /**
     * slice()
     * @param args
     */
    public static void main1(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6});
        byteBuffer.position(2);
        ByteBuffer newBuffer = byteBuffer.slice();
        System.out.println(newBuffer.position() + " " + newBuffer.limit() + " " + newBuffer.capacity());
//        byteBuffer.put(2, (byte) 9);
//        for(int i=0; i< newBuffer.limit(); i++){
//            System.out.println(newBuffer.get(i));
//        }
        newBuffer.put(0, (byte) 10);
        System.out.println(byteBuffer.get(2));

        System.out.println("byteBuffer.arrayOffset()=" + byteBuffer.arrayOffset() + " newBuffer.arrayOffSet()=" + newBuffer.arrayOffset());

        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(10);
        System.out.println(allocateDirect.arrayOffset());
    }

    public static void main(String[] args) {
//        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
//        System.out.println(byteBuffer.mark());
//        CharBuffer charBuffer = byteBuffer.asCharBuffer();
//        System.out.println(charBuffer.position());
//        System.out.println(charBuffer.limit());
//        System.out.println(charBuffer.capacity());
//        System.out.println(charBuffer.mark());
//        System.out.println(Charset.defaultCharset().name());
//        byte[] bytes = "我是中国人".getBytes();
//        byte[] bytes = "我是中国人".getBytes(StandardCharsets.UTF_16BE);
//        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
//        System.out.println(byteBuffer.position() + " " + byteBuffer.limit() + " " + byteBuffer.capacity());
//        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
//        System.out.println(charBuffer.position() + " " + charBuffer.limit() + " " + charBuffer.capacity());
//
//        for(int i=0; i<byteBuffer.limit(); i++){
//            System.out.print(byteBuffer.get(i));
//        }
//        System.out.println();
//        for(int j=0; j<charBuffer.limit(); j++){
//            System.out.print(charBuffer.get(j));
//        }
//
//        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6});
//        System.out.println(byteBuffer1.position() + " " + byteBuffer1.limit() + " " + byteBuffer1.capacity());
//        byteBuffer1.flip();
////        byteBuffer1.rewind();
////        byteBuffer1.clear();
//        System.out.println(byteBuffer1.position() + " " + byteBuffer1.limit() + " " + byteBuffer1.capacity());
//        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
//        byteBuffer.put((byte) 1);
//        ByteBuffer slice = byteBuffer.slice();
//        System.out.println(slice.position() + " " + slice.limit() + " " + slice.capacity());
//        CharBuffer charBuffer = byteBuffer.asCharBuffer();
//        System.out.println(charBuffer.position() + " " + charBuffer.limit() + " " + charBuffer.capacity());
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.putDouble(1.1D);
        byteBuffer.putDouble(1.2D);
        byteBuffer.putDouble(1.3D);
//        byteBuffer.putDouble(1.4D);
        byteBuffer.flip();
        ByteBuffer slice = byteBuffer.slice();
        System.out.println(slice.position() + " " + slice.limit() + " " + slice.capacity());
//        System.out.println(byteBuffer.position() + " " + byteBuffer.limit() + " " + byteBuffer.capacity());
//        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
//        System.out.println(doubleBuffer.position() + " " + doubleBuffer.limit() + " " + doubleBuffer.capacity());
//        for(int i=0; i<doubleBuffer.capacity(); i++){
//            System.out.println(doubleBuffer.get(i));
//        }
//        System.out.println("-------------------------------");

    }
    /**
     * slice()、asChaBuffer()、asShortBuffer()、asIntBuffer()、asLongBuffer()、asFloatBuffer()、asDoubleBuffer()
     * 核心要点：1.新缓冲区的内容从此缓冲区的position开始，新缓冲区的limit为老缓冲区的remaining()的 1/n n为进制数(slice()可以理解为n=1)
     * 2. 新复制的缓冲区与原缓冲区共享底层数组
     */




}
