package com.bigDragon.javase.ioStream.BIO.InetAddress;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;

/**
 * @author: bigDragon
 * @create: 2025/3/28
 * @Description:
 */

public class NioServer2 {
    private static final ExecutorService workerPool = Executors.newFixedThreadPool(4); // 业务线程池
    
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8899));
        serverChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    buffer.clear();
                    int bytesRead = client.read(buffer);
                    
                    if (bytesRead == -1) {
                        client.close();
                    } else if (bytesRead > 0) {
                        // 异步处理数据写入（不阻塞事件循环）
                        buffer.flip();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        workerPool.submit(() -> processData(data)); // 提交到线程池
                    }
                }
            }
        }
    }
    
    private static void processData(byte[] data) {
        // 模拟耗时操作（如写入文件、业务逻辑处理）
        try {
            Thread.sleep(100); // 假设耗时 100ms
            System.out.println("处理完成，数据长度: " + data.length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
