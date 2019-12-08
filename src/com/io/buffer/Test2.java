package com.io.buffer;

import java.nio.CharBuffer;

/**
 * @author lubaijiang
 */
public class Test2 {
    /**
     * clear()、flip()、rewind()
     *
     * @param args
     */
    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put("918纪念日");
        /**
         * clear()方法的侧重点是初始化一切状态
         */
//        System.out.println(charBuffer.capacity() + " " + charBuffer.limit() + " " + charBuffer.position());
//        charBuffer.clear();
//        System.out.println(charBuffer.capacity() + " " + charBuffer.limit() + " " + charBuffer.position());

        /**
         * flip()侧重点是substring
         */
//        System.out.println(charBuffer.capacity() + " " + charBuffer.limit() + " " + charBuffer.position());
//        charBuffer.flip();
//        System.out.println(charBuffer.capacity() + " " + charBuffer.limit() + " " + charBuffer.position());

        /**
         * rewind()方法侧重点是重新，在重新写入、重新读时使用
         */
        System.out.println(charBuffer.capacity() + " " + charBuffer.limit() + " " + charBuffer.position());
        charBuffer.rewind();
        System.out.println(charBuffer.capacity() + " " + charBuffer.limit() + " " + charBuffer.position());

    }
}
