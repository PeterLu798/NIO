package com.io.buffer;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * @author lubaijiang
 */
public class CharBufferMain {

    public static void main(String[] args) throws IOException {
        CharBuffer charBuffer = CharBuffer.wrap("abcd");
        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());

        CharBuffer charBuffer1 = CharBuffer.allocate(2);
        charBuffer.read(charBuffer1);

        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());

        System.out.println(charBuffer1.position());
        System.out.println(charBuffer1.limit());
    }

    public static void main1(String[] args) throws IOException {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.append('a');  //等同于 charBuffer.put('a');
        System.out.println(charBuffer.position());
        charBuffer.append("ddd"); //等同于 charBuffer.put("ddd");
        System.out.println(charBuffer.position());
        charBuffer.append("efghi", 0, 1); //等同于 charBuffer.put("efghi", 0, 1);
        System.out.println(charBuffer.position());

        charBuffer.flip();
        System.out.println(charBuffer.charAt(0));

        CharBuffer charBuffer1 = CharBuffer.allocate(3);
        charBuffer1.put("ab");

        // read方法
        CharBuffer charBuffer2 = CharBuffer.allocate(3);
        charBuffer1.read(charBuffer2);

        //wrap
        System.out.println("--------------------------------------------");
        CharBuffer abcdefg = CharBuffer.wrap("abcdefg", 1, 2);
        System.out.println(abcdefg.position() + " " + abcdefg.limit() + " " + abcdefg.capacity() + " " + abcdefg.isReadOnly());
//        for(int i=0; i<abcdefg.capacity(); i++){
//            System.out.println(abcdefg.get(i));
//        }
        System.out.println("=====================");
        for(int i=0; i<abcdefg.limit(); i++){
            System.out.println(abcdefg.get(i));
        }

        CharBuffer charBuffer3 = CharBuffer.wrap("12345");
        System.out.println(charBuffer3.position() + " " + charBuffer3.limit() + " " + charBuffer3.capacity());
        charBuffer3.length();
    }
    /**
     * 1. append(char c) 等同于 put(char c)
     * append(CharSequence csq) 等同于 put(String s)
     * append(CharSequence csq, int start, int end) 等同于 put(str.subString(start, end))
     * 2. read(CharBuffer target)
     * 源代码如下：
     * (1) 如果this.remaining() ==0的话说明没有可读取的字符，返回-1
     * (2) 取 n = Math.min(this.remaining(), target.remaining())
     * (3) 判断如果target.remaining小于this.remaining()的话，则将this的limit缩小为：this.position + n
     * (4) 判断如果 n>0 （此步的目的是确认target的可用空间大于0）则 target.put(this)【这里是使用了 put(CharBuffer c)方法】
     * (5) 最后恢复this的limit
     * 3. wrap(CharSequence csq, int start, int end)
     * (1) 此方法产生的缓冲区是只读缓冲区
     * (2) 新缓冲区的capacity为csq的长度
     * (3) 新缓冲区的position 为 start
     * (4) 新缓冲区的limit为 end
     * 4. length() 等同于 remaining()
     */
}
