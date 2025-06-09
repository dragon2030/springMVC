package com.bigDragon.javase.ioStream.NIO.selector;


import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

//Selector示例 客户端 服务端
public class SelectorDemo2 {

    //标准的 NIO 多路复用服务端
    @Test
    public void serverDemo() throws Exception {
        //1 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //2 切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //3 创建buffer
        ByteBuffer serverByteBuffer = ByteBuffer.allocate(1024);

        //4 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(8080));

        //5 获取selector选择器
        Selector selector = Selector.open();

        //6 通道注册到选择器，进行监听
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //7 选择器进行轮询，进行后续操作
        while(selector.select()>0) {//好像会一直阻塞？
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while(selectionKeyIterator.hasNext()) {
                //获取就绪操作
                SelectionKey next = selectionKeyIterator.next();
                //判断什么操作
                if(next.isAcceptable()) {
                    System.out.println("服务端isAcceptable");
                    //serverSocketChannel.accept() 接受客户端连接，返回 SocketChannel。
                    SocketChannel accept = serverSocketChannel.accept();

                    //切换非阻塞模式
                    accept.configureBlocking(false);

                    //注册
                    accept.register(selector,SelectionKey.OP_READ);
                    
                } else if(next.isReadable()) {
                    System.out.println("服务端isReadable");
                    SocketChannel channel = (SocketChannel) next.channel();

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    //读取数据
                    int length = 0;
                    while((length = channel.read(byteBuffer))>0) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(),0,length));
                        byteBuffer.clear();
                    }

                }

                selectionKeyIterator.remove();
            }
        }
    }

    //客户端代码
    @Test
    public void clientDemo() throws Exception {
        //1 获取通道，绑定主机和端口号
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));

        //2 切换到非阻塞模式,注意 客户端使用 Selector（多路复用）才需要设置成阻塞否则不需要阻塞
        socketChannel.configureBlocking(false);

        //3 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //4 写入buffer数据
        byteBuffer.put(new Date().toString().getBytes());

        //5 模式切换
        byteBuffer.flip();

        //6 写入通道
        socketChannel.write(byteBuffer);

        //7 关闭
        byteBuffer.clear();
    }

    //用于实现客户端循环输入的过程，对比clientDemo实现改进，不止一次交互
    @Test
    public void clientDemo2() throws Exception {
        //1 获取通道，绑定主机和端口号
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1",8081));

        //2 切换到非阻塞模式
        socketChannel.configureBlocking(false);

        //3 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String str = scanner.next();

            //4 写入buffer数据
            byteBuffer.put((new Date().toString()+"--->"+str).getBytes());

            //5 模式切换
            byteBuffer.flip();

            //6 写入通道
            socketChannel.write(byteBuffer);

            //7 关闭
            byteBuffer.clear();
            //服务端isAcceptable
            //服务端isReadable
            //Sun May 18 18:29:33 CST 2025--->a
            //服务端isReadable
            //Sun May 18 18:29:40 CST 2025--->b
        }

    }
}
