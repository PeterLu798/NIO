package com.io.socket;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.List;

public class DatagramSocketClient {
    /**
     * UDP发送数据：
     * 1. 新建一个DatagramSocket实例
     * 2. 使用connect方法将其连接到要接收数据的一端
     * 3. 新建 DatagramPacket 实例，使用构造函数 DatagramPacket(byte[], int)将要发送的数据填充
     * 4. 调用socket.send(packet)方法发送
     *
     */
    public static void main1(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.connect(new InetSocketAddress("localhost", 8888));
        byte[] bytes = "12345678".getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.send(packet);
        socket.close();
    }

    /**
     * 数据包最大为 65507，大于的话会报异常
     */
    public static void main2(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.connect(new InetSocketAddress("localhost",8888));
        byte[] bytes = new byte[65508];
        for(int i= 0; i< 65504; i++){
            bytes[i] = 'a';
        }
        bytes[65504] = 'e';
        bytes[65505] = 'n';
        bytes[65506] = 'd';

        bytes[65507] = 'z';

        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.send(packet);

        socket.close();
    }

    /**
     * 获取本机广播ip
     * 127.255.255.255
     * 192.168.199.255
     * 192.168.153.255
     * 192.168.88.255
     */
    public static void mainGetBroadcast(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
            for(InterfaceAddress interfaceAddress : interfaceAddresses){
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if(broadcast != null){
                    System.out.println(broadcast.getHostAddress());
                }
            }
        }
    }

    /**
     * 使用connect(InetAddress, int)方法实现UDP广播
     * 上述4个IP全部能发送成功
     */
    public static void main3(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("192.168.88.255"), 7777);
        DatagramPacket packet = new DatagramPacket("1234__".getBytes(), 6);
        socket.send(packet);

        socket.close();
    }

    public static void main(String[] args) throws IOException {
        MulticastSocket socket = new MulticastSocket();
        byte[] bytes = "abcde__".getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("224.0.0.5"), 8888);
        socket.send(packet);
        socket.close();
    }
}
