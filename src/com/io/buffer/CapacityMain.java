package com.io.buffer;

import java.nio.*;

/**
 * @author lubaijiang
 */
public class CapacityMain {
    public static void main(String[] args) {
        byte[] bytes = new byte[]{1, 2, 3};
        short[] shorts = new short[]{1,2,3};
        int[] ints = new int[]{1, 2, 3};
        long[] longs = new long[]{1, 2, 3};
        double[] doubles = new double[]{1.1, 2.2, 3.3};
        float[] floats = new float[]{1.1F, 2.2F, 3.3F};
        char[] chars = new char[]{'a', 'b', 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        ShortBuffer shortBuffer = ShortBuffer.wrap(shorts);
        IntBuffer intBuffer = IntBuffer.wrap(ints);
        LongBuffer longBuffer = LongBuffer.wrap(longs);
        DoubleBuffer doubleBuffer = DoubleBuffer.wrap(doubles);
        FloatBuffer floatBuffer = FloatBuffer.wrap(floats);
        CharBuffer charBuffer = CharBuffer.wrap(chars);

        System.out.println("byteBuffer = " + byteBuffer.getClass().getName());
        System.out.println("shortBuffer = " + shortBuffer.getClass().getName());
        System.out.println("intBuffer = " + intBuffer.getClass().getName());
        System.out.println("longBuffer = " + longBuffer.getClass().getName());
        System.out.println("doubleBuffer = " + doubleBuffer.getClass().getName());
        System.out.println("floatBuffer = " + floatBuffer.getClass().getName());
        System.out.println("charBuffer = " + charBuffer.getClass().getName());

        System.out.println();

        System.out.println("byteBuffer.capacity = " + byteBuffer.capacity());
        System.out.println("shortBuffer.capacity = " + shortBuffer.capacity());
        System.out.println("intBuffer.capacity = " + intBuffer.capacity());
        System.out.println("longBuffer.capacity = " + longBuffer.capacity());
        System.out.println("doubleBuffer.capacity = " + doubleBuffer.capacity());
        System.out.println("floatBuffer.capacity = " + floatBuffer.capacity());
        System.out.println("charBuffer.capacity = " + charBuffer.capacity());

        


    }
}
