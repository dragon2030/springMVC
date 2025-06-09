package com.bigDragon.javase.ioStream.NIO.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 Selector示例 客户端 服务端
 多路复用连接
 */
public class SelectorDemo3 {
    
    public static void main(String[] args) throws Exception {
        // 创建 Selector
        Selector selector = Selector.open();
        
        // 1. 连接主服务端口（8080）
        SocketChannel mainServiceChannel = SocketChannel.open();
        mainServiceChannel.configureBlocking(false);
        mainServiceChannel.connect(new InetSocketAddress("localhost", 8080));
        mainServiceChannel.register(selector, SelectionKey.OP_CONNECT, "MainService");
        
        // 2. 连接管理端口（9090）
        SocketChannel adminChannel = SocketChannel.open();
        adminChannel.configureBlocking(false);
        adminChannel.connect(new InetSocketAddress("localhost", 9090));
        adminChannel.register(selector, SelectionKey.OP_CONNECT, "AdminService");
        
        // 用户输入线程（发送数据）
        new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.println("请输入要发送的数据（格式：端口|消息，如 8080|Hello）：");
                    String input = scanner.nextLine();
                    String[] parts = input.split("\\|");
                    if (parts.length != 2) {
                        System.err.println("输入格式错误！");
                        continue;
                    }
                    int port = Integer.parseInt(parts[0]);
                    String message = parts[1];
                    
                    // 根据端口选择通道
                    SocketChannel targetChannel = null;
                    for (SelectionKey key : selector.keys()) {
                        if (key.channel() instanceof SocketChannel) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            if (channel.socket().getPort() == port) {
                                targetChannel = channel;
                                break;
                            }
                        }
                    }
                    
                    if (targetChannel != null && targetChannel.isConnected()) {
                        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                        targetChannel.write(buffer);
                    } else {
                        System.err.println("通道未就绪或端口无效！");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        
        // 事件处理循环
        while (true) {
            selector.select(); // 阻塞直到有事件就绪
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove(); // 必须移除！
                
                try {
                    if (key.isConnectable()) {
                        handleConnect(key, selector);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                } catch (Exception e) {
                    key.cancel();
                    if (key.channel() != null) {
                        key.channel().close();
                    }
                    System.err.println("连接异常: " + e.getMessage());
                }
            }
        }
    }
    
    // 处理连接完成
    private static void handleConnect(SelectionKey key, Selector selector) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        String serviceName = (String) key.attachment();
        
        if (channel.finishConnect()) {
            System.out.println(serviceName + " 连接成功: " + channel.getRemoteAddress());
            channel.register(selector, SelectionKey.OP_READ); // 监听读事件
        } else {
            System.err.println(serviceName + " 连接失败！");
        }
    }
    
    // 处理数据读取
    private static void handleRead(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        String serviceName = (String) key.attachment();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = channel.read(buffer);
        
        if (bytesRead == -1) { // 服务端关闭连接
            System.out.println(serviceName + " 服务端断开连接");
            channel.close();
            return;
        }
        
        buffer.flip();
        String message = new String(buffer.array(), 0, bytesRead);
        System.out.println(serviceName + " 收到响应: " + message);
    }
}
