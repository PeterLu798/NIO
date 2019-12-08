package com.io.socket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class DatagramSocketServer {
    /**
     * UDP接受请求：
     * 1. 新建一个 DatagramPacket(byte[], int length)并且约定接受的数据长度为length
     * 2. 调用 DatagramSocket实例的 receive(DatagramPacket packet)方法
     * 3. 调用packet.getData()取出数据
     * 注意：
     * 约定长度很重要
     *
     */
    public static void main1(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(8888);
        byte[] bytes = new byte[12];

        DatagramPacket packet = new DatagramPacket(bytes, 10);

        socket.receive(packet);
        socket.close();

        System.out.println("包中的数据长度：" + packet.getLength());
        System.out.println(new String(packet.getData()));
    }

    public static void main2(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(8888);
        byte[] bytes = new byte[65508];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\1022.txt"));
        outputStream.write(packet.getData());

        outputStream.close();
        socket.close();
    }

    public static void main3(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(7777);
        byte[] bytes = new byte[10];
        DatagramPacket packet = new DatagramPacket(bytes, 10);
        socket.receive(packet);
        System.out.println("server1接收到的数据：" + new String(packet.getData()));

        socket.close();
    }

    /**
     * 接受组播信息：
     * 1.MulticastSocket类是DatagramSocket的子类
     * 2.使用joinGroup方法将要组播的机器加入
     * 3.组播的所有机器都要有相同的端口号
     */
    public static void main(String[] args) throws IOException {
        MulticastSocket socket = new MulticastSocket(8888);
        socket.joinGroup(InetAddress.getByName("224.0.0.5"));
        byte[] bytes = new byte[10];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        System.out.println("receive msg: " + new String(packet.getData()));
        socket.close();
    }
}