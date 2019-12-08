package com.io.selector;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lubaijiang
 */
public class ServerSocketChannel1 {
    /**
     * 验证 bind(SocketAddress endpoint) 方法
     *
     * @param args
     * @throws IOException
     */
    public static void main1(String[] args) throws IOException {
        char[] chars = new char[1024];
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(new InetSocketAddress("localhost", 8888));
        Socket accept = socket.accept();
        InputStream inputStream = accept.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int length = reader.read(chars);
        while (length != -1) {
            System.out.println(new String(chars));
            length = reader.read(chars);
        }

        reader.close();
        inputStream.close();
        serverSocketChannel.close();
    }

    /**
     * 验证bind方法的backlog参数，但并未成功
     */
    public static void main2(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888), 60);
        ServerSocket serverSocket = serverSocketChannel.socket();
        Thread.sleep(5000);
        boolean isRun = true;
        while (isRun) {
            System.out.println("开始连接");
            Socket accept = serverSocket.accept();
            accept.close();
            System.out.println("连接结束");
        }

        Thread.sleep(8000);
        serverSocket.close();
        serverSocketChannel.close();
    }

    /**
     * accept()方法的阻塞性，与通道的阻塞性（通过configureBlocking(boolean)方法设置）的关系：
     * 1.如果通道是阻塞的：没有连接时处于阻塞状态
     * 2.如果通道是非阻塞的：没有连接时直接返回null
     * 3.此方法的优势是返回一个SocketChannel对象，可以注册到选择器中实现多路复用
     */
    public static void main3(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        //true为阻塞，也是默认选项
        serverSocketChannel.configureBlocking(true);
        System.out.println("开始阻塞。。。。");
        //使用ServerSocketChannel的accept方法
        SocketChannel socket = serverSocketChannel.accept();
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        int read = socket.read(byteBuffer);
        while (read != -1) {
            System.out.println(new String(byteBuffer.array()));
            byteBuffer.flip();
            read = socket.read(byteBuffer);
        }
        //使用ServerSocketChannel的socket方法
        /*ServerSocket serverSocket = serverSocketChannel.socket();
        Socket accept = serverSocket.accept();
        char[] chars = new char[2];
        InputStream inputStream = accept.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int read1 = reader.read(chars);
        while (read1 != -1) {
            System.out.println(new String(chars));
            read1 = reader.read(chars);
        }*/
        System.out.println("结束阻塞。。。。");
        socket.close();
        serverSocketChannel.close();
    }

    /**
     * register(Selector sel, int ops)方法的作用是注册到选择器上
     * 参数sel就是要注册的选择器
     * 参数ops代表方法返回值SelectionKey的可用操作集
     *
     */
    public static void main4(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        //如果为阻塞通道，则register方法会报 IllegalBlockingModeException
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("selector=" + selector);
        System.out.println("key=" + selectionKey);
        serverSocketChannel.close();
    }

    /**
     * isRegistered方法判断是否已注册
     * 注意：当selectionKey执行取消操作或通道执行close操作后，调用isRegistered方法可能依然返回true
     * 这是因为延迟造成的
     */
    public static void main5(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        //判断是否已经注册
        System.out.println("first=="+serverSocketChannel.isRegistered());
        SelectionKey selectionKey = serverSocketChannel.register(Selector.open(), SelectionKey.OP_ACCEPT);
        //判断是否已经注册
        System.out.println("second=="+serverSocketChannel.isRegistered());
        selectionKey.cancel();
        //判断是否已经注册
        System.out.println("third=="+serverSocketChannel.isRegistered());
        serverSocketChannel.close();
        //判断是否已经注册
        System.out.println("fourth=="+serverSocketChannel.isRegistered());
    }

    /**
     * blockingLock返回的这个锁是configureBlocking和register方法所使用的锁，用于防止重复操作
     */
    public static void main6(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //blockingLock返回的这个锁是configureBlocking和register方法所使用的锁，用于防止重复操作
        Object o = serverSocketChannel.blockingLock();
        System.out.println(o);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(Selector.open(), SelectionKey.OP_ACCEPT);
        serverSocketChannel.close();
    }

    /**
     * supportedOptions获得支持的SocketOption列表
     */
    public static void main7(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        SocketChannel socketChannel = serverSocketChannel.accept();
        Set<SocketOption<?>> options = serverSocketChannel.supportedOptions();
        Set<SocketOption<?>> socketOptions = socketChannel.supportedOptions();
        System.out.println("serverSocketChannel操作选项：");
        for (SocketOption socketOption : options){
            System.out.println(socketOption.name() + " " + socketOption.getClass().getName());
        }
        System.out.println("socketChannel操作选项：");
        for (SocketOption socketOption : socketOptions){
            System.out.println(socketOption.name() + " " + socketOption.getClass().getName());
        }
        socketChannel.close();
        serverSocketChannel.close();
    }

    /**
     * 获取与设置SocketOption
     * 获取：getOption
     * 设置：setOption
     */
    public static void main8(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Integer option = serverSocketChannel.getOption(StandardSocketOptions.SO_RCVBUF);
        System.out.println(option);
        serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 2048);
    }

    /**
     *  获得SocketAddress对象：getLocalAddress()
     *  判断阻塞模式：isBlocking()
     */
    public static void main9(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        //注意这里需要强转一下InetSocketAddress
        InetSocketAddress localAddress = (InetSocketAddress) serverSocketChannel.getLocalAddress();
        System.out.println(localAddress.getAddress());
        System.out.println(localAddress.getHostString());
        System.out.println(localAddress.getPort());
        //判断阻塞模式
        System.out.println(serverSocketChannel.isBlocking());
        serverSocketChannel.configureBlocking(false);
        System.out.println(serverSocketChannel.isBlocking());
        serverSocketChannel.close();
        System.out.println(serverSocketChannel.isBlocking());
    }

    /**
     * ServerSocketChannel.open()：方法返回的是同一个provider，但是不同的通道
     * Selector.open()：方法返回的是通过一个provider，但是不同的选择器
     * ServerSocketChannel对象的keyFor(Selector)方法：返回注册了该选择器的SelectionKey对象
     */
    public static void main10(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
        //open方法返回的是同一个provider，但是不同的通道
        System.out.println(serverSocketChannel.provider() == serverSocketChannel1.provider());
        System.out.println(serverSocketChannel == serverSocketChannel1);

        Selector selector = Selector.open();
        Selector selector1 = Selector.open();
        //open方法返回的是通过一个provider，但是不同的选择器
        System.out.println(selector.provider() == selector1.provider());
        System.out.println(selector == selector1);

        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        SelectionKey selectionKey1 = serverSocketChannel.register(selector1, SelectionKey.OP_ACCEPT);
        SelectionKey selectionKey2 = serverSocketChannel.keyFor(selector);
        SelectionKey selectionKey3 = serverSocketChannel.keyFor(selector1);
        //keyFor
        System.out.println(selectionKey == selectionKey2);
        System.out.println(selectionKey1 == selectionKey3);

    }

    /**
     * register源码解析
     */
    public static void main11(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        //register源码解读：全局有一个SelectionKey数组，先循环遍历这个数组，如果某一个selectionKey的selector==传入的selector的话则返回，
        //否则new一个SelectionKey实例，然后将其加入到数组中，并返回这个实例
        //这个方法以及关键子方法全部是加锁同步的。
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(SelectionKey.OP_ACCEPT & ~serverSocketChannel.validOps());
    }

    /**
     * SocketChannel对象
     */
    public static void main12(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        //accept返回值 SocketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.close();
        serverSocketChannel.close();
        System.out.println("连接结束");
    }


    public static void main13(String[] args) throws IOException {
        //TODO
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = null;
        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if(selectionKey.isAcceptable()){
                    socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                }
                if(selectionKey.isWritable()){
                    RandomAccessFile file = new RandomAccessFile("E:\\tmp\\CentOS-7-x86_64-DVD-1810.iso", "rw");
                    System.out.println("file.length=" + file.length());
                    FileChannel channel = file.getChannel();
                    channel.transferTo(0, file.length(), socketChannel);
                    channel.close();
                    file.close();
                    socketChannel.close();
                }

            }
        }
    }

    /**
     * 传输大文件
     *
     */
    public static void main14(String[] args) throws IOException {
        //TODO
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        Selector selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){

        }
    }

    /**
     * read / write 方法
     * 结合com.io.selector.Client1#main8(java.lang.String[])得出：
     * 当通道是非阻塞时，read和write方法也是非阻塞的
     *
     */
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        int select = selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            ServerSocketChannel channel = (ServerSocketChannel)key.channel();
            SocketChannel accept = channel.accept();
            accept.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            System.out.println("start: " + System.currentTimeMillis());
            accept.read(byteBuffer);
            System.out.println("end: " + System.currentTimeMillis());
            System.out.println(byteBuffer.position() + " " + byteBuffer.limit());

        }
        serverSocketChannel.close();
    }

}
