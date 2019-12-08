package com.io.buffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lubaijiang
 */
public class OtherMain {
    public static void main(String[] args) {
        /**
         * isReadOnly()方法
         */
//        char[] chars = new char[]{'a', 'b', 'c'};
//        byte[] bytes = new byte[]{1, 2, 3};
//        int[] ints = new int[]{1, 2, 3};
//        CharBuffer charBuffer = CharBuffer.wrap(chars);
//        System.out.println(charBuffer.isReadOnly());
//        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
//        System.out.println(byteBuffer.isReadOnly());
//        IntBuffer intBuffer = IntBuffer.wrap(ints);
//        System.out.println(intBuffer.isReadOnly());
        /**
         * isDirect()方法
         */
//        System.out.println(charBuffer.isDirect());
//        System.out.println(byteBuffer.isDirect());
//        System.out.println(intBuffer.isDirect());
//        //创建直接缓冲区 只有ByteBuffer才能创建直接缓冲区
//        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(100);
//        System.out.println(byteBuffer1.isDirect());
//        //clear()状态初始化，并不是清除数据
//        charBuffer.limit(2);
//        charBuffer.position(2);
//        charBuffer.mark();
//        charBuffer.clear();
//        System.out.println(charBuffer.limit() + " " + charBuffer.position());
        /**
         * hasArray()方法
         */
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
//        System.out.println(byteBuffer.hasArray());
//        CharBuffer charBuffer = CharBuffer.allocate(1);
//        System.out.println(charBuffer.hasArray());
//        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(1);
//        System.out.println(byteBuffer2.hasArray());
        /**
         * hasRemaining()
         */
//        System.out.println(byteBuffer.hasRemaining());
        /**
         * arrayOffSet()
         */
//        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3});
//        System.out.println(byteBuffer.arrayOffset());

        /**
         * List.toArray()
         */
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{1,2, 3});
        ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[]{2,4,2});
        ByteBuffer byteBuffer3 = ByteBuffer.wrap(new byte[]{6,7,9});
        List<ByteBuffer> list = new ArrayList<>();
        list.add(byteBuffer1);
        list.add(byteBuffer2);
        list.add(byteBuffer3);

        ByteBuffer[] array = new ByteBuffer[list.size()];
        list.toArray(array);
        System.out.println(list.size());

        for(int i=0; i<array.length; i++){
            //不用flip()，因为创建Buffer时是直接初始化数组
//            array[i].flip();
            ByteBuffer byteBuffer4 = array[i];
            while (byteBuffer4.hasRemaining()){
                System.out.println(byteBuffer4.get());
            }
            System.out.println("------------------------");
        }

    }

    /**
     * 1.isReadOnly()
     * 2.isDirect()
     * 3.hasArray()
     * 4.hasRemaining()
     * 5.arrayOffset()
     * 6.list.toArray() / list.toArray(T[] t)
     */
}
