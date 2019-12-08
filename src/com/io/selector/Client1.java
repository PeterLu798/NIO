package com.io.selector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author lubaijiang
 */
public class Client1 {

    public static void main1(String[] args) throws IOException {
        for (int i=0; i<100; i++){
            Socket socket = new Socket("localhost", 8888);
            socket.close();
            System.out.println("客户端连接个数为：" + (i+1));
        }
    }

    public static void main2_3(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("ddddddddddddd".getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static void main7(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8888));
        socketChannel.write(ByteBuffer.wrap("1234".getBytes()));
        socketChannel.close();
    }

    public static void main9(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7777);
        socket.close();
    }

}
