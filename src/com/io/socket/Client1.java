package com.io.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) throws IOException {
        System.out.println("client开始连接=" + System.currentTimeMillis());
        Socket socket = new Socket("localhost", 8088);
        System.out.println("client结束连接=" + System.currentTimeMillis());
        socket.close();

        try {
            socket = new Socket("www.csdn.net", 80);
            System.out.println("连接成功");
            socket = new Socket("www.ffffsssss.net", 80);
        } catch (IOException e) {
            System.out.println("连接失败");
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) throws IOException, InterruptedException {
        System.out.println("client start " + System.currentTimeMillis());
        Socket socket = new Socket("localhost", 8088);
        System.out.println("client end " + System.currentTimeMillis());
        Thread.sleep(5000);
        socket.close();
    }

    public static void main2(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 8088);
        OutputStream outputStream = socket.getOutputStream();
        //第一次输出
        outputStream.write("我是lbj1".getBytes());
        //第二次输出
        Thread.sleep(3000);
        outputStream.write("我是lbj2".getBytes());
        //第三次输出
        Thread.sleep(3000);
        outputStream.write("我是lbj3".getBytes());

        outputStream.close();
    }

    public static void main3(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8088);
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int read = inputStream.read(bytes);
        while (read != -1) {
            System.out.println(new String(bytes));
            read = inputStream.read(bytes);
        }
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("嘿嘿嘿".getBytes());

        inputStream.close();
        outputStream.close();
    }
}
