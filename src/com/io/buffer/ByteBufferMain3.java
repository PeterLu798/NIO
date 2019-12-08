package com.io.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author lubaijiang
 */
public class ByteBufferMain3 {
    public static void main1(String[] args) {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
//        ByteOrder order = byteBuffer.order();
//        ArrayList<Integer> objects = new ArrayList<>();
//        objects.add(1);
//        Object[] objects1 = objects.toArray();
//        for(int i=0; i<objects1.length; i++){
//            System.out.println(objects1[i]);
//        }
//        Integer[] array = new Integer[3];
//        Integer[] integers = objects.toArray(array);
//        for(int i=0; i<array.length; i++){
//            System.out.println(array[i]);
//        }
//        System.out.println("-----------------------------");
//        for(int j=0; j<integers.length; j++){
//            System.out.println(integers[j]);
//        }
//        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
//        System.out.println(byteBuffer.order());
//        int value = 123456789;
//        byteBuffer.putInt(value);
//        byte[] array = byteBuffer.array();
//        for(int i=0; i< array.length; i++){
//            System.out.print(array[i] + " ");
//        }
//        System.out.println();
//        ByteBuffer byteBuffer1 = ByteBuffer.allocate(4);
//        byteBuffer1.order(ByteOrder.LITTLE_ENDIAN);
//        byteBuffer1.putInt(value);
//        byte[] array1 = byteBuffer1.array();
//        for(int i=0; i< array1.length; i++){
//            System.out.print(array1[i] + " ");
//        }
//        System.out.println();
//        ByteBuffer byteBuffer2 = ByteBuffer.allocate(4);
//        byteBuffer2.order(ByteOrder.LITTLE_ENDIAN);
//        byteBuffer2.putInt(1234);
//        byte[] array2 = byteBuffer2.array();
//        for (int i=0; i<array2.length; i++){
//            System.out.print(array2[i] + " ");
//        }
//        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
//        byteBuffer.order(ByteOrder.BIG_ENDIAN);
//        byteBuffer.putInt(123);
//        byteBuffer.putInt(456);
//        byteBuffer.flip();
//        System.out.println(byteBuffer.getInt());
//        System.out.println(byteBuffer.getInt());
//
//        ByteBuffer byteBuffer1 = ByteBuffer.wrap(byteBuffer.array());
//        byteBuffer1.order(ByteOrder.LITTLE_ENDIAN);
//        System.out.println(byteBuffer1.getInt());
//        System.out.println(byteBuffer1.getInt());

//        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
//        ByteBuffer byteBuffer1 = byteBuffer.asReadOnlyBuffer();
//        byteBuffer1.put((byte) 9);

        //1.使用wrap创建的
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6});
        System.out.println("A " + byteBuffer.position() + " " + " " + byteBuffer.limit() + " " + byteBuffer.capacity());
        byteBuffer.get();
        System.out.println("B " + byteBuffer.position() + " " + " " + byteBuffer.limit() + " " + byteBuffer.capacity());
        byteBuffer.get();
        System.out.println("C " + byteBuffer.position() + " " + " " + byteBuffer.limit() + " " + byteBuffer.capacity());
        byteBuffer.compact();
        System.out.println("D " + byteBuffer.position() + " " + " " + byteBuffer.limit() + " " + byteBuffer.capacity());
        byteBuffer.flip();
        for (int i = 0; i < byteBuffer.limit(); i++) {
            System.out.println(byteBuffer.get());
        }
        //2.使用allocate创建的
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(8);
    }

    /**
     * 模拟ByteBuffer equals()
     * @param object
     * @return
     */
    public boolean equals(Object object) {
//        if (object == this) {
//            return true;
//        }
//        if (!(object instanceof ByteBuffer)) {
//            return false;
//        }
//        ByteBuffer that = (ByteBuffer) object;
//        if(this.remaining() != that.remaining()){
//            return false;
//        }
//        for(int i =this.position(), j=that.position(); i < this.limit(); i++, j++){
//            if(that.get(j) != this.get(i)){
//                return false;
//            }
//        }
        return true;
    }

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{7, 8, 9});
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{4, 5, 1, 2, 3});
        byteBuffer1.get();
        byteBuffer1.get();
        System.out.println(byteBuffer.equals(byteBuffer1));
        System.out.println(byteBuffer.compareTo(byteBuffer1));

        byteBuffer.duplicate();
        byteBuffer.slice();

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * 1. order()：获取缓冲区的字节顺序
     * (1) 默认为从高位到低位：ByteOrder.BIG_ENDIAN
     * 2. order(ByteOrder order)：设置字节顺序
     * (1) 两个选项：高位到低位：ByteOrder.BIG_ENDIAN
     * 低位到高位：ByteOrder.LITTLE_ENDIAN
     * (2) 先设置顺序在put值才起作用
     * 3. compact()；压缩
     * (1) 将remaining之间的元素依次顺移到缓冲区0位置往后
     * (2) 此时position为remaining
     * (3) limit和capacity不变
     * 4. equals() / compareTo()
     * (1) 相同点：都是判断remaining()之间的字符是否相等
     * (2) 不同点：返回值不同：equals返回true/false表示相等/不相等；而compareTo()返回 0/非0表示相等/不想等
     * 其二这两个方法具体实现方式也不同
     * 5. equals(Object object)源码
     * (1) 上来判断 object == this，如果是的话直接返回true (注意：object为null时此行不报空指针)
     * (2) 判断 object instanceof ByteBuffer, 如果object不是ByteBuffer的实例，直接返回false（这一步为之后的强转做判断）
     * (3) 强转object的类型为ByteBuffer
     * (4) 接下来就要比较两个缓冲区remaining()之间的字符是否相等了，这个相等包括：一长度相等，二每个对应位置的字符相等
     * if(this.remaining() != object.remaining()) return false;
     * 然后循环判断每个相应位置的字符相等，只要出现一个不相等的立即返回false
     * for(int i =this.position(), j=object.position(); i < this.limit(); i++, j++){
     *     if(object.get(j) != this.get(i)){
     *        return false;
     *      }
     * }
     * 6. duplicate() / slice()
     * (1) duplicate() 复制一个与原缓冲区一模一样的缓冲区，包括position,limit,capacity等都相同；
     * 这两个缓冲区共享数组，但是position,limit,capacity是独立的
     * (2) slice()是复制remaining之间的字符，新生成的缓冲区position为0，limit和capacity为remaining
     */
}
