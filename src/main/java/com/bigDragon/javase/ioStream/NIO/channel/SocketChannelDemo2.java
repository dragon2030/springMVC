package com.bigDragon.javase.ioStream.NIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author: bigDragon
 * @create: 2025/5/15
 * @Description:
 */
public class SocketChannelDemo2 {
    //Client 端代码
    public void client() {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.connect(new InetSocketAddress("localhost", 8080));
        
            ByteBuffer buffer = ByteBuffer.wrap("Hello Server".getBytes());
            while (buffer.hasRemaining()) {
                channel.write(buffer);  // Buffer → Channel
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Server 端代码
    public void Server() throws IOException {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(8080));
            SocketChannel clientChannel = serverChannel.accept();
        
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = clientChannel.read(buffer);  // Channel → Buffer
        
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            System.out.println("Received: " + new String(data));
        }
    }
}
