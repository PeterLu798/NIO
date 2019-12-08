package com.io.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author lubaijiang
 */
public class SelectionKey1 {
    /**
     * isValid()方法：
     * 执行了cancel之后就会返回false，不用等待select
     * 而三大集合必须等select之后才能完全注销
     * 注意与通道类所提供的方法validOps()方法的区别：validOps()方法的作用是支持的注册事件
     * interestOps()方法：
     * 返回注册的那个事件
     * readyOps()方法：
     * 返回已经准备就绪的事件
     */
    public static void main1(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println(serverSocketChannel.validOps());
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //验证isValid方法
//        selectionKey.cancel();
//        System.out.println(selectionKey.isValid());
        int interestOps = selectionKey.interestOps();
        System.out.println(interestOps);
        System.out.println(SelectionKey.OP_ACCEPT);

        int readyOps = selectionKey.readyOps();
        System.out.println("readyOps=" + readyOps);
    }

    /**
     *
     *
     *
     */
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        boolean isRun = true;
        while (isRun){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //防止重复消费
                iterator.remove();
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = socketChannel.read(byteBuffer);
                    while (read != -1){
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.clear();
                        read = socketChannel.read(byteBuffer);
                    }
                    socketChannel.write(ByteBuffer.wrap("accept over".getBytes()));

                    socketChannel.close();
                    channel.close();
                }
            }
        }
        serverSocketChannel.close();

    }
}
