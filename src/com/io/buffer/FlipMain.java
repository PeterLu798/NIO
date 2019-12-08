package com.io.buffer;

import java.nio.CharBuffer;

/**
 * @author lubaijiang
 */
public class FlipMain {
    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(20);
        charBuffer.put("NIO初学者");
        for (int i = 0; i < charBuffer.limit(); i++) {
            System.out.println(charBuffer.get(i));
        }
        System.out.println("-----------------over----------------");
        //初始化状态
        charBuffer.clear();
        charBuffer.put("abccccccccccc");
        //flip方法相当于：charBuffer.limit(position); charBuffer.position(0);
        charBuffer.flip();
        //get()方法获取当前position处的元素，然后将position加1
        for (int i = 0; i < charBuffer.limit(); i++) {
            System.out.println(charBuffer.get());
        }
        System.out.println("----------------over2---------------");

    }
}
