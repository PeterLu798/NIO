package com.io.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lubaijiang
 */
public class Selector1 {
    public static void main1(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel.configureBlocking(false);

        System.out.println(1);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(2);

        int select = selector.select();

        System.out.println(3 + " select=" + select);

        serverSocketChannel.close();

        System.out.println(4 + " end");

    }

    /**
     * 如果循环中没有处理accept事件，那么会呈死循环，这时select()是非阻塞的
     */
    public static void main2(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int select = selector.select();

            Set<SelectionKey> keys = selector.keys();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("select=" + select);
            System.out.println("keys.size=" + keys.size());
            System.out.println("selectionKeys.size=" + selectionKeys.size());

            for (SelectionKey selectionKey : selectionKeys) {
                ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                channel.accept();
            }
        }

    }

    /**
     * 防止accept()重复消费：使用iterator.remove()方法
     * while循环里面的keys集合对象一直是同一个，不会随着循环体创建新的集合
     * while循环里面的selectionKeys集合对象一直是同一个，不会随着循环体创建新的集合
     */
    public static void main3(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocketChannel serverSocketChannel2 = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress("localhost", 7777));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel2.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel2.configureBlocking(false);

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel2.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();
            Set<SelectionKey> keys = selector.keys();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("select=" + select);
            System.out.println("keys.size=" + keys.size());
            System.out.println("selectionKeys.size=" + selectionKeys.size());

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = channel.accept();
                System.out.println("socketChannel= " + socketChannel);

                InetSocketAddress localAddress = (InetSocketAddress) channel.getLocalAddress();
                System.out.println(localAddress.getPort() + " 被客户端连接了！");

                //解决重复无效消费
                iterator.remove();
            }

//            for (SelectionKey selectionKey : selectionKeys) {
//                ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
//                SocketChannel socketChannel = channel.accept();
//                System.out.println("socketChannel= " + socketChannel);
//
////                if (socketChannel == null) {
////                    System.out.println("打印这条信息证明是连接8888服务器时，重复消费的情况发生");
////                    System.out.println("将7777关联的SelectionKey对应的SocketChannel通道取出来，但是值为null");
////                }
//                InetSocketAddress localAddress = (InetSocketAddress) channel.getLocalAddress();
//                System.out.println(localAddress.getPort() + " 被客户端连接了！");
//
//                //解决重复无效消费
//
//            }
            System.out.println();
        }
    }

    /**
     * select()方法返回值的含义：
     * 非0：表示新增的已就绪的通道数量
     * 0：表示没有新增的已就绪的通道
     */
    public static void main4(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 7777));
        serverSocketChannel.configureBlocking(false);

        ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
        serverSocketChannel1.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel1.configureBlocking(false);

        ServerSocketChannel serverSocketChannel2 = ServerSocketChannel.open();
        serverSocketChannel2.bind(new InetSocketAddress("localhost", 9999));
        serverSocketChannel2.configureBlocking(false);

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel1.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel2.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();
            Set<SelectionKey> keys = selector.keys();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("select=" + select);
            System.out.println("keys.size=" + keys.size());
            System.out.println("selectionKeys.size=" + selectionKeys.size());

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = channel.accept();
//                socketChannel.read()
            }
            Thread.sleep(10000);
        }
    }

    /**
     * 同一个通道注册不同的事件，返回的SelectionKey对象是同一个对象
     * 如下例中的socketChannel分别注册了读事件和写事件，返回的是同一个对象
     */
    public static void main5(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 7777));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey selectionKey3 = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel1.accept();
                socketChannel.configureBlocking(false);

                SelectionKey selectionKey1 = socketChannel.register(selector, SelectionKey.OP_READ);

                SelectionKey selectionKey2 = socketChannel.register(selector, SelectionKey.OP_WRITE);

                System.out.println(selectionKey1 == selectionKey2);
                System.out.println(selectionKey1 == selectionKey3);
            }

        }
    }

    /**
     * isOpen()方法：只有当选择器打开时返回true
     * 获取provider对象
     * keys()方法：返回此选择器的键集
     */
    public static void main6(String[] args) throws IOException {
        Selector selector = Selector.open();
        System.out.println(selector.isOpen());
        selector.close();
        System.out.println(selector.isOpen());

        SelectorProvider provider = Selector.open().provider();
        SelectorProvider provider1 = SelectorProvider.provider();
        System.out.println(provider == provider1);

        Selector selector1 = Selector.open();
        Set<SelectionKey> keys = selector1.keys();
        System.out.println(keys.size());
    }

    /**
     * select(long timeout)方法：
     * 阻塞timeout毫秒之后自动返回
     * selectNow()：是非阻塞的
     */
    public static void main7(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            System.out.println("进入第一个while循环: " + System.currentTimeMillis());
//            int select = selector.select(5000);
            int select = selector.selectNow();
            System.out.println("select(long)返回值：" + select + " 时间：" + System.currentTimeMillis());
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                System.out.println("进入第二个while循环");
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    System.out.println(socketChannel);
                }
                iterator.remove();
            }
        }
    }

    /**
     * wakeup()方法：用于一个线程唤醒另一个线程当处于select()（或select(long)）阻塞时，此时select方法会直接返回
     */
    private static Selector selector;
    public static void main(String[] args) throws IOException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    selector.wakeup();
                    Set<SelectionKey> keys = selector.keys();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    System.out.println("执行wakeup之后的selector的信息");
                    System.out.println("keys.size=" + keys.size());
                    System.out.println("selectionKeys.size=" + selectionKeys.size());
                }catch (Exception e){

                }
            }
        });
        t.start();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        register.cancel();

        while (true){
            int select = selector.select();

            System.out.println("wakeup唤醒，直接返回：" + select);
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                System.out.println("有就绪的通道");
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if(selectionKey.isAcceptable()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    int read = socketChannel.read(byteBuffer);
                    while (read != -1){
                        String string = new String(byteBuffer.array());
                        System.out.println(string);
                        byteBuffer.clear();
                        read = socketChannel.read(byteBuffer);
                    }
                }
            }
        }

    }

}
