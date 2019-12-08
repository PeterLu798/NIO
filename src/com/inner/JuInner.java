package com.inner;

public class JuInner {
    public static void main(String[] args) {
        int aa = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(aa);
            }
        }).start();
    }
}
