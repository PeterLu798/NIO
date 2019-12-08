package com.io.netty;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class FirstNetty {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        Selector selector2 = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        for (; ; ) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    socketChannel.register(selector2, SelectionKey.OP_READ);
                }
            }
        }


    }
}
