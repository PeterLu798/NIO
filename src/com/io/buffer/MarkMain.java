package com.io.buffer;

import java.nio.ByteBuffer;

/**
 * @author lubaijiang
 */
public class MarkMain {
    public static void main(String[] args) {
        byte[] bytes = new byte[]{1, 2, 3, 4};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.mark();
        byteBuffer.position(1);
        System.out.println("A " + byteBuffer.position());
        byteBuffer.reset();
        System.out.println("B " + byteBuffer.position());
    }
}
