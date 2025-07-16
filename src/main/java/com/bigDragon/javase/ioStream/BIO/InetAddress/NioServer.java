package com.bigDragon.javase.ioStream.BIO.InetAddress;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: bigDragon
 * @create: 2025/3/28
 * @Description:
 * 服务端工作流程
 *      初始化
 *          创建 ServerSocketChannel 并绑定端口，设置为非阻塞模式。
 *          注册 Selector 监听 OP_ACCEPT 事件（新连接请求）。
 *      事件循环
 *          selector.select() 阻塞，直到有 Channel 事件就绪（如新连接或数据到达）。
 *          遍历 selectedKeys 处理就绪事件。
 *      处理事件
 *          Accept 事件：接受客户端连接，注册 OP_READ 事件。
 *          Read 事件：从客户端 Channel 读取数据到 Buffer，处理数据。
 * 
 * 问题一：为什么需要有这个通道serverChannel呢，客户端连接不是应该在服务端只创建一个通道嘛，bio的时候也只会有一个线程处理客户端请求
 * ServerSocketChannel负责监听新的连接事件，而每个SocketChannel负责各自的读写事件，这样Selector可以统一管理所有事件，实现单线程或多线程的高效处理。
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建 ServerSocketChannel 并绑定端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8899));
        serverSocketChannel.configureBlocking(false); // 非阻塞模式
        
        // 2. 创建 Selector 并注册 Accept 事件
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        System.out.println("服务端启动，等待连接...");
        ByteBuffer buffer = ByteBuffer.allocate(1024); // 缓冲区
        
        while (true) {
            selector.select(); // 阻塞直到至少一个 Channel 有事件就绪，返回就绪的 Channel 数量。
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove(); // 必须移除已处理的 key
                
                if (key.isAcceptable()) {
                    // 3. 处理 Accept 事件：接受客户端连接
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false); // 客户端通道非阻塞
                    clientChannel.register(selector, SelectionKey.OP_READ); // 注册读事件
                    System.out.println("客户端连接: " + clientChannel.getRemoteAddress());
                    
                } else if (key.isReadable()) {
                    // 4. 处理 Read 事件：读取客户端数据
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    buffer.clear(); // 清空缓冲区
                    int bytesRead = clientChannel.read(buffer);
                    
                    if (bytesRead == -1) {
                        // 客户端关闭连接
                        clientChannel.close();
                        System.out.println("客户端断开连接");
                    } else if (bytesRead > 0) {
                        // 写入文件（此处简化，实际需处理文件流）
                        buffer.flip(); // 切换为读模式
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        System.out.println("收到数据: " + new String(data));
                        buffer.clear(); // 清空缓冲区，准备下一次读取
                    }
                }
            }
        }
    }
}
