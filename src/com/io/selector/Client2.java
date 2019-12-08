package com.io.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author lubaijiang
 */
public class Client2 {
    public static void main1(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress("localhost", 8888));
        boolean isRun = true;
        while (isRun){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if(selectionKey.isConnectable()){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    while (!channel.finishConnect()){
                        System.out.println("正在连接...");
                    }
                    channel.write(ByteBuffer.wrap("怎么回子事??".getBytes()));

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = channel.read(byteBuffer);
                    while (read != -1){
                        System.out.println(new String(byteBuffer.array()));
                        byteBuffer.clear();
                        read = channel.read(byteBuffer);
                    }
                    channel.close();
                }
            }
        }
        socketChannel.close();
    }
}
