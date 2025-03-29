package com.bigDragon.javase.InetAddress;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * @author: bigDragon
 * @create: 2025/3/28
 * @Description:
 */
public class 
NioClient {
    public static void main(String[] args) throws IOException {
        // 1. 创建 SocketChannel 并连接服务端
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false); // 非阻塞模式
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
        
        // 等待连接完成
        while (!socketChannel.finishConnect()) {
            // 可在此处执行其他任务（非阻塞特性）
        }
        System.out.println("连接服务端成功");
        
        // 2. 发送数据
        String message = "Hello, NIO Server!";
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer); // 非阻塞写入，可能需多次调用
        }
        System.out.println("数据发送完成");
        
        // 3. 关闭连接
        socketChannel.close();
    }
}
