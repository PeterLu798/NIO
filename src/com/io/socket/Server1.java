package com.io.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;

public class Server1 {
    /**
     * (1) Socket类的getInputStream()方法可以获得输入流InputStream，该类的read()方法同样具有阻塞性
     * (2) 允许多次调用write()方法进行写入
     * (3) 调用Stream的close()方法造成Socket关闭
     * (4) TCP的3次握手与4次挥手
     * (5) 服务端与客户端互传对象以及I/O流顺序问题
     */

    public static void main(String[] args) throws IOException {
        //ServerSocket类的accept()方法
        ServerSocket serverSocket = new ServerSocket(8083);
        System.out.println("开始阻塞");
        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String getString = "";
        while (null != (getString = bufferedReader.readLine()) && getString.trim().length() > 0) {
            System.out.println(getString);
        }

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
        outputStream.write("<html><body><a href='http://www.baidu.com'>baidu.com</a></body></html>".getBytes());
        outputStream.flush();

        inputStream.close();
        outputStream.close();
        socket.close();
        serverSocket.close();
        System.out.println("==============关闭连接=============");

    }

    public static void main1(String[] args) throws IOException {
        //验证(1)，结合Client1.main1方法
        //read()方法阻塞的原因是：Server端一直在尝试读取Client发送的数据，但是Client从未发过数据，所以Server端一直阻塞
        byte[] bytes = new byte[1024];
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("accept start " + System.currentTimeMillis());
        Socket socket = serverSocket.accept();
        System.out.println("accept end " + System.currentTimeMillis());
        InputStream inputStream = socket.getInputStream();
        System.out.println("read start " + System.currentTimeMillis());
        int read = inputStream.read(bytes);
        System.out.println("read end " + System.currentTimeMillis());

        inputStream.close();
        socket.close();
        serverSocket.close();
    }

    public static void main2(String[] args) throws IOException {
        //验证(2)，结合Client1.main2方法
        CharBuffer buffer = CharBuffer.allocate(15);
        ServerSocket serverSocket = new ServerSocket(8088);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int read = reader.read(buffer);
        while (read != -1){
            System.out.println(new String(buffer.array()));
            buffer.clear();
            read = reader.read(buffer);
        }
        inputStream.close();
        serverSocket.close();
    }

    public static void main3(String[] args) throws IOException {
        //验证(3)，结合Client1.main3方法
        ServerSocket serverSocket = new ServerSocket(8088);
        Socket socket = serverSocket.accept();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("哈哈哈".getBytes());
        outputStream.close();

        InputStream inputStream = socket.getInputStream();

        socket.close();
        serverSocket.close();
    }
}
