package com.io.buffer;

import java.nio.CharBuffer;

/**
 * @author lubaijiang
 */
public class PositionMain {
    public static void main(String[] args) {
        char[] chars = new char[]{'a', 'b', 'c', 'd'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println(charBuffer.position());
        charBuffer.position(2);
        charBuffer.put('1');
        for (int i = 0; i < chars.length; i++){
            System.out.println(i + " " + chars[i]);
        }
    }
}
