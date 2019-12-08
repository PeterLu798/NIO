package com.io.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lubaijiang
 */
public class SocketChannel1 {
    /**
     * 阻塞模式下调用connect方法
     * 1.当连接失败时直接抛出异常
     * 2.消耗时间相比较非阻塞模式要长：原因是阻塞模式下方法内部发起3次握手之后才返回，而非阻塞模式是直接返回
     */
    public static void main1(String[] args) throws IOException {
        long start = 0;
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            start = System.currentTimeMillis();
            boolean connect = socketChannel.connect(new InetSocketAddress("localhost", 8888));
            System.out.println("正常完成时间: " + (System.currentTimeMillis() - start) + " 返回值：" + connect);
        } catch (Exception e) {
            System.out.println("异常完成时间: " + (System.currentTimeMillis() - start) + " 返回值：");
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }

    /**
     * 非阻塞模式下调用connect方法
     * 1. 连接失败时不会抛出异常但返回值是false
     * 2. 消耗时间相比阻塞模式很小
     * 3. 连接成功时也返回false，这也充分证明了非阻塞模式下没经过3次握手直接返回了结果
     */
    public static void main2(String[] args) throws IOException {
        long start = 0;
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            //非阻塞模式
            socketChannel.configureBlocking(false);
            start = System.currentTimeMillis();
            boolean connect = socketChannel.connect(new InetSocketAddress("localhost", 8888));
            System.out.println("正常完成时间: " + (System.currentTimeMillis() - start) + " 返回值：" + connect);
        } catch (Exception e) {
            System.out.println("异常完成时间: " + (System.currentTimeMillis() - start) + " 返回值：");
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }

    /**
     * isConnectionPending
     * 该方法的作用是检测是否【正在】进行连接
     * 在阻塞模式和非阻塞模式下的区别
     * 阻塞模式下：连接成功返回true，连接失败返回false
     * 非阻塞模式下：只要调用则返回true，表示正在连接
     */
    public static void main3(String[] args) throws IOException {
        SocketChannel socketChannel = null;
        //阻塞模式下不存在的地址isConnectionPending方法返回值
//        try {
//            socketChannel = SocketChannel.open();
//            System.out.println(socketChannel.isConnectionPending());
//            socketChannel.connect(new InetSocketAddress("192.168.333.333", 8888));
//        } catch (IOException e) {
//            System.out.println("fail: " + socketChannel.isConnectionPending());
//            e.printStackTrace();
//        } finally {
//            socketChannel.close();
//        }

        //非阻塞模式下不存在地址isConnectionPending方法返回值
//        socketChannel = SocketChannel.open();
//        socketChannel.configureBlocking(false);
//        System.out.println(socketChannel.isConnectionPending());
//        //不存在的地址
//        socketChannel.connect(new InetSocketAddress("192.168.0.123", 8888));
//        System.out.println(socketChannel.isConnectionPending());

        //阻塞模式下存在的地址isConnectionPending方法返回值
//        socketChannel = SocketChannel.open();
//        System.out.println(socketChannel.isConnectionPending());
//        socketChannel.connect(new InetSocketAddress("localhost", 8888));
//        System.out.println(socketChannel.isConnectionPending());

        //非阻塞模式下存在的地址isConnectionPending方法返回值
//        socketChannel = SocketChannel.open();
//        socketChannel.configureBlocking(false);
//        socketChannel.connect(new InetSocketAddress("localhost", 8888));
//        System.out.println(socketChannel.isConnectionPending());

    }

    public static void main4(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        boolean connect = socketChannel.connect(new InetSocketAddress("localhost", 8888));
        if (!connect) {
            int i = 0;
            try {
                while (!socketChannel.finishConnect()) {
                    i++;
                    System.out.println("正在尝试连接");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("连接次数：" + i);
        }
        socketChannel.close();

    }

    public static void main5(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        boolean connect = socketChannel.connect(new InetSocketAddress("localhost", 8888));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                    serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
                    SocketChannel socketChannel1 = serverSocketChannel.accept();

                    socketChannel1.close();
                    serverSocketChannel.close();
                    System.out.println("server end");
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        if(! connect){
            while (!socketChannel.finishConnect()){
                System.out.println("正在尝试连接");
            }
        }
        System.out.println("客户端连接成功");
        socketChannel.close();
        System.out.println(socketChannel.finishConnect());

    }

    /**
     * finishConnect()方法
     * 结合main4、main5得出以下结论：
     * 1.没建立连接之前调用此方法会抛出 NoConnectionPendingException
     * 2.在连接close之后调用会抛出 ClosedChannelException
     * 3.此方法的作用是完成SocketChannel连接过程
     *
     */
    public static void main6(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
//        System.out.println(socketChannel.finishConnect());
        socketChannel.connect(new InetSocketAddress("localhost", 8888));
        System.out.println(socketChannel.finishConnect());
        socketChannel.close();
        System.out.println(socketChannel.finishConnect());
    }

    public static void main13(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8888));
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true){
            System.out.println("begin selector");
            if(socketChannel.isOpen()){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isConnectable()){
                        while (!socketChannel.finishConnect()){
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if(selectionKey.isReadable()){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(50000);
                        int read = socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        while (read != -1){
                            read = socketChannel.read(byteBuffer);

                        }
                    }
                }
            }
        }
    }

    /**
     * 这种写法是错误的，open(SocketAddress)内部已经调用了connect方法
     * 再设置一些option已经没有作用了
     *
     */
    public static void main7(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8888));
        socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1234);
    }
}
