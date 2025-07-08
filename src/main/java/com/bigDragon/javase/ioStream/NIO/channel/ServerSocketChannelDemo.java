package com.bigDragon.javase.ioStream.NIO.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 ServerSocketChannel使用 建立非阻塞的SocketChannel通道
 不使用 Selector 的限制 1/轮询效率问题：2/代码复杂度 3/扩展性问题
 适用场景 连接数非常少的简单应用 或者 学习或演示 
 */
public class ServerSocketChannelDemo {

    public static void main(String[] args) throws Exception {
        //端口号
        int port = 8888;

        //buffer 创建一个ByteBuffer，内容为"hello atguigu"字符串的字节形式
        ByteBuffer buffer = ByteBuffer.wrap("hello atguigu".getBytes());

        //ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //绑定到指定的端口
        ssc.bind(new InetSocketAddress(port));
        
        //设置非阻塞模式
        ssc.configureBlocking(false);

        //监听有新链接传入
        while(true) {
            System.out.println("Waiting for connections");
            //接受连接，如果是在阻塞模式运行，会一直阻塞在accept方法，没有链接传入时不会返回null
            SocketChannel sc = ssc.accept();
            if(sc == null) { //在非阻塞模式下，accept()方法会立即返回，如果没有连接则返回null
                System.out.println("null");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from: " + sc.socket().getRemoteSocketAddress());
                buffer.rewind(); //指针0,重置buffer的position到0（rewind()）
                sc.write(buffer);
                sc.close();
            }
        }
    }
    //Waiting for connections
    //null
    //Waiting for connections
    //null
    //Waiting for connections
    //Incoming connection from: /0:0:0:0:0:0:0:1:60704 //浏览器请求时返回
    //Waiting for connections
    //Incoming connection from: /0:0:0:0:0:0:0:1:60705
}
