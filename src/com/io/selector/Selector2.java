package com.io.selector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lubaijiang
 */
public class Selector2 {
    /**
     * 细节：
     * 1. 只要register了就会在selector.keys集合中
     * 2. 当且仅当register并且调用了select方法之后才能在selector.selectionKeys集合中
     * 3. select()方法是阻塞的，只有客户端连接了或者其他线程wakeup了或者其他线程interrupt了才返回
     * 4. selectionKey.cancel方法只有在调用了selector.select()之后才起作用
     * 5. selectionKey.cancel方法起作用后，会将上述1、2集合中的通道元素删除掉
     */
    public static void main1(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 7777));
        serverSocketChannel.configureBlocking(false);

        ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
        serverSocketChannel1.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel1.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel1.register(selector, SelectionKey.OP_ACCEPT);

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("localhost", 7777);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("我要发往客户端7777".getBytes());
                    socket.close();

                    Socket socket1 = new Socket("localhost", 8888);
                    OutputStream outputStream1 = socket1.getOutputStream();
                    outputStream1.write("我要发往客户端8888".getBytes());
                    socket1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        client.start();

        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    Set<SelectionKey> keys = selector.keys();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    System.out.println();
                    System.out.println("select()方法执行第2次后的信息：");
                    System.out.println("keys.size=" + keys.size());
                    System.out.println("selectionKeys.size=" + selectionKeys.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        getInfo.start();

        Thread.sleep(1000);

        while (true) {
            int select = selector.select();
            Set<SelectionKey> keys = selector.keys();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("取消之前的信息：");
            System.out.println("keys.size=" + keys.size());
            System.out.println("selectionKeys.size=" + selectionKeys.size());
            System.out.println("select=" + select);
            System.out.println();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
//                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel accept = channel.accept();
                    int read = accept.read(byteBuffer);
                    while (read != -1) {
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.clear();
                        read = accept.read(byteBuffer);
                    }
                    if (channel.socket().getLocalPort() == 7777) {
                        selectionKey.cancel();
                        System.out.println("取消之后的信息");
                        System.out.println("keys.size=" + keys.size());
                        System.out.println("selectionKeys.size=" + selectionKeys.size());
                    }
                }
            }
        }
    }

    /**
     * 1.取消之后的通道，在执行select方法期间注销，通过查看select方法的源码得知：select方法内部会首先删除所有取消的通道
     * 2.select方法的作用就是确定已准备就绪的通道，所谓准备就绪是指 客户端与服务端已经通过3次握手建立了连接，那么这个通道就是已就绪通道
     */
    public static void main2(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
        serverSocketChannel1.bind(new InetSocketAddress("localhost", 7777));
        serverSocketChannel1.configureBlocking(false);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel1.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        boolean run = true;
        while (run) {
            //阻塞
            selector.select();
            Set<SelectionKey> keys = selector.keys();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("close之前");
            System.out.println("keys.size=" + keys.size());
            System.out.println("selectionKeys.size=" + selectionKeys.size());

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = socketChannel.read(byteBuffer);
                    while (read != -1) {
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.clear();
                        read = socketChannel.read(byteBuffer);
                    }
                    socketChannel.close();
                    if (channel.socket().getLocalPort() == 7777) {
                        channel.close();
                    }
                    System.out.println("close之后，select之前");
                    System.out.println("keys.size=" + keys.size());
                    System.out.println("selectionKeys.size=" + selectionKeys.size());
                }
            }
        }

        serverSocketChannel.close();
        serverSocketChannel1.close();
    }

    /**
     * 细节3：
     * 1.新创建的选择器中，3个集合都是空集合
     * 2.直接调用set集合的remove方法删除键集中的集合时，会报UnsupportedOperationException
     * 3.Set是线程不安全的
     */
    public static void main3(String[] args) throws IOException, InterruptedException {
        Set<Integer> set = new HashSet<>(1);
        set.add(1);
        set.remove(1);
        System.out.println(set.size());

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 7777));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        Set<SelectionKey> keys = selector.keys();
        //会抛出异常
//        keys.remove(selectionKey);
        System.out.println(keys.size());

        Set<String> set1 = new HashSet<>();
        set1.add("a");
        set1.add("b");
        set1.add("c");
        set1.add("d");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    set1.remove("c");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        Iterator<String> iterator = set1.iterator();
        while (iterator.hasNext()) {
            Thread.sleep(1000);
            System.out.println(iterator.next());
        }

    }

    /**
     * 1. 多线程中，选择器的close方法会中断close方法的阻塞
     * 2. 多线程中，使用线程的interrupt方法会中断close方法的阻塞
     * 3. 调用选择器的close方法会注销所有通道
     */
}