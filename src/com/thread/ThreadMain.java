package com.thread;

/**
 * @author lubaijiang
 */
public class ThreadMain extends Thread{


    public static void main(String[] args) {
        ThreadMain thread = new ThreadMain();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
        System.out.println("当前线程是否被打断：" + Thread.interrupted());
        System.out.println("当前线程是否被打断：" + Thread.interrupted());

        thread.interrupt();
        System.out.println("thread是否被打断：" + thread.isInterrupted());
        System.out.println("thread是否被打断：" + thread.isInterrupted());

    }

    @Override
    public void run() {
        for (int i=0; i<5000000;){
//            System.out.println("i=" + i);
            i++;
        }
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
