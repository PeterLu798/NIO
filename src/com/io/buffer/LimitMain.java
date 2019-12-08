package com.io.buffer;

import java.nio.ByteBuffer;

/**
 * @author lubaijiang
 */
public class LimitMain {
    public static void main(String[] args) {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println("capacity== " + byteBuffer.capacity());
        System.out.println("limit== " + byteBuffer.limit());
        byteBuffer.limit(3);

        System.out.println("capacity== " + byteBuffer.capacity());
        System.out.println("limit== " + byteBuffer.limit());

        byteBuffer.put(0, (byte) 6);
        byteBuffer.put(1, (byte) 7);
        byteBuffer.put(2, (byte) 8);
        byteBuffer.put(3, (byte) 9);
        byteBuffer.put(4, (byte) 10);
        byteBuffer.put(5, (byte) 11);
        byteBuffer.put(6, (byte) 12);
    }
}
