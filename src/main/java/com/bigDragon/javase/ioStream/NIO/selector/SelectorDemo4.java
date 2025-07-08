package com.bigDragon.javase.ioStream.NIO.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 完整的NIO示例，包含服务端和客户端代码，演示了
     1/客户端发送数据
     2/服务端接收并回应
     3/客户端接收服务端响应并打印
 
 服务端流程：
    监听连接请求(ACCEPT)
    接收客户端数据(READ)
    处理并回应数据(WRITE)
 客户端流程：
    建立连接(CONNECT)
    发送用户输入数据(WRITE)
    接收服务端响应(READ)
    循环交互
 Selector作用：
    单线程管理多个Channel
    通过事件驱动方式处理I/O
    避免为每个连接创建线程
 
 注意：只要客户端不断开连接，服务端和客户端就能持续保持双向通信。这是 NIO 的典型工作模式。
 */
//服务端代码 
public class SelectorDemo4 {
    public void NIOServer() throws Exception {
        // 1. 创建Selector
        Selector selector = Selector.open();
        
        // 2. 创建ServerSocketChannel并绑定端口
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(8080));
        serverSocket.configureBlocking(false);
        
        // 3. 将serverSocket注册到selector，监听ACCEPT事件
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started on port 8080...");
        
        while (true) {
            // 4. 阻塞等待就绪的Channel
            selector.select();
            
            // 5. 获取就绪的SelectionKey集合
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                
                if (key.isAcceptable()) {
                    // 6. 处理ACCEPT事件
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    
                    // 7. 将客户端socket注册到selector，监听READ事件
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Client connected: " + client.getRemoteAddress());
                    
                } else if (key.isReadable()) {
                    // 8. 处理READ事件
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = client.read(buffer);
                    
                    if (bytesRead == -1) {
                        // 客户端断开连接
                        key.cancel();
                        client.close();
                        System.out.println("Client disconnected");
                        continue;
                    }
                    
                    // 9. 读取数据并回应
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String received = new String(bytes);
                    System.out.println("Received from client: " + received);
                    
                    // 10. 准备响应数据
                    String response = "Server response: " + received.toUpperCase();
                    ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
                    client.write(responseBuffer);
                }
                
                // 11. 移除已处理的key
                iter.remove();
            }
        }
    }
    
    //客户端代码
    public void NIOClient() throws Exception {
        // 1. 创建Selector
        Selector selector = Selector.open();
        
        // 2. 创建SocketChannel并连接服务器
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));
        
        // 3. 注册CONNECT事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            // 4. 等待事件发生
            selector.select();
            
            // 5. 获取就绪的SelectionKey集合
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                
                if (key.isConnectable()) {
                    // 6. 处理连接完成
                    SocketChannel channel = (SocketChannel) key.channel();
                    
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    
                    channel.configureBlocking(false);
                    
                    // 7. 提示用户输入消息
                    System.out.print("Enter message to send (type 'exit' to quit): ");
                    String message = scanner.nextLine();
                    
                    if ("exit".equalsIgnoreCase(message)) {
                        channel.close();
                        System.out.println("Client closed");
                        return;
                    }
                    
                    // 8. 发送消息到服务器
                    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                    channel.write(buffer);
                    
                    // 9. 注册READ事件以接收响应
                    channel.register(selector, SelectionKey.OP_READ);
                    
                } else if (key.isReadable()) {
                    // 10. 处理服务器响应
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = channel.read(buffer);
                    
                    if (bytesRead == -1) {
                        channel.close();
                        continue;
                    }
                    
                    // 11. 打印服务器响应
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String response = new String(bytes);
                    System.out.println("Server response: " + response);
                    
                    // 12. 准备下一次输入
                    System.out.print("Enter message to send (type 'exit' to quit): ");
                    String message = scanner.nextLine();
                    
                    if ("exit".equalsIgnoreCase(message)) {
                        channel.close();
                        System.out.println("Client closed");
                        return;
                    }
                    
                    // 13. 发送新消息并继续监听响应
                    buffer = ByteBuffer.wrap(message.getBytes());
                    channel.write(buffer);
                }
            }
        }
    }
}
