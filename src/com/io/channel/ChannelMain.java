package com.io.channel;

import java.io.*;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractInterruptibleChannel;

/**
 * @author lubaijiang
 */
public class ChannelMain {
    public static void main1(String[] args) {
        AutoCloseable autoCloseable;
        Closeable closeable;
        Channel channel;
        //异步
        AsynchronousChannel asynchronousChannel ;
        AsynchronousByteChannel asynchronousByteChannel;
        AsynchronousSocketChannel asynchronousSocketChannel;

        //同步
        ReadableByteChannel readableByteChannel;
        ScatteringByteChannel scatteringByteChannel;

        WritableByteChannel writableByteChannel;
        GatheringByteChannel gatheringByteChannel;

        ByteChannel byteChannel;
        SeekableByteChannel seekableByteChannel;

        NetworkChannel networkChannel;
        MulticastChannel multicastChannel;

        InterruptibleChannel interruptibleChannel;

        //实现类
        AbstractInterruptibleChannel abstractInterruptibleChannel;
        Pipe.SinkChannel sinkChannel;

        FileChannel fileChannel;

        ServerSocketChannel serverSocketChannel;
        SocketChannel socketChannel;


    }

    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\tmp\\a.txt"));
        FileChannel channel = fileOutputStream.getChannel();
        channel.lock(0,4,true);
    }

    /**
     *
     */
}
