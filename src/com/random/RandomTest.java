package com.random;

import java.util.Random;

/**
 * @author lubaijiang
 * 随机函数测试
 */
public class RandomTest {
    public static void main1(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            System.out.println(Math.random());
//        }

        Random random = new Random();
//        Random random1 = new Random(10);
        for(int i=0; i<4; i++){
            System.out.println(random.nextInt(20) +1);
//            System.out.println(random1.nextInt(30));
        }
    }

    public static void main(String[] args) {
        System.out.println(8 % 7);
        System.out.println(-8 % 7);
    }
}
