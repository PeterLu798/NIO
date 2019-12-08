package com.io.buffer;

import java.nio.CharBuffer;

/**
 * @author lubaijiang
 */
public class RemainingMain {
    public static void main(String[] args) {
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println(charBuffer.remaining());
        charBuffer.position(1);
        System.out.println(charBuffer.remaining());
        charBuffer.limit(2);
        System.out.println(charBuffer.remaining());
    }
}
