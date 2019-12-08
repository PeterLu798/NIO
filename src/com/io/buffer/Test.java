package com.io.buffer;

import java.nio.ByteBuffer;

/**
 * @author lubaijiang
 */
public class Test {
    /**
     * allocate()、wrap()、limit()、position()、mark()、reset()、put()
     * @param args
     */
    public static void main(String[] args) {
        /**
         * capacity不能为负数
         */
//        try {
//            ByteBuffer.allocate(-1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        byte[] bytes = new byte[]{1, 2, 3, 4};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        /**
         * limit不能为负数
         */
//        try {
//            byteBuffer.limit(-1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * position不能为负数
         */
//        try {
//            byteBuffer.position(-1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * position不能大于limit
         */
//        try {
//            byteBuffer.limit(2);
//            byteBuffer.position(3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * limit不能大于capacity
         */
//        try {
//            byteBuffer.limit(byteBuffer.capacity() +1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * 当position小于mark时，mark会失效
         */
//        try {
//            byteBuffer.position(3);
//            byteBuffer.mark();
//            byteBuffer.position(2);
//            byteBuffer.reset();
//            System.out.println(byteBuffer.position());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * limit小于mark时，mark会失效
         */
//        try {
//            byteBuffer.limit(3);
//            byteBuffer.position(3);
//            byteBuffer.mark();
//            System.out.println("A " + byteBuffer.position());
//            byteBuffer.limit(2);
//            System.out.println("B " + byteBuffer.position());
//            byteBuffer.reset();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * 如果没有mark，reset失败
         */
//        try {
//            byteBuffer.reset();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * 当limit设置的值小于position时position的值会被赋为新的limit的值
         */
//        byteBuffer.position(3);
//        System.out.println(byteBuffer.position());
//        byteBuffer.limit(2);
//        System.out.println(byteBuffer.position());
        /**
         * 当position和limit相等时，在该位置插入值会报异常
         */
        try {
            byteBuffer.position(2);
            byteBuffer.limit(2);
            byteBuffer.put((byte) 10);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
