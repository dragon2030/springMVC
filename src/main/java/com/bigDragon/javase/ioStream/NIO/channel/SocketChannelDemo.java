package com.bigDragon.javase.ioStream.NIO.channel;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {

    public static void main(String[] args) throws Exception {
        //创建SocketChannel 两种方式效果相同，只是步骤不同。方法一更简洁，方法二可以更灵活地控制连接过程。
        //方法一
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));
        //方法二
//        SocketChannel socketChanne2 = SocketChannel.open();
//        socketChanne2.connect(new InetSocketAddress("www.baidu.com", 80));
    
        /**
         * 设置阻塞和非阻塞
         * 阻塞模式（true）：I/O 操作会阻塞线程直到完成
         * 非阻塞模式（false）：I/O 操作立即返回，可能读取不到数据或只读取部分数据
         */
        socketChannel.configureBlocking(false);
        
        //************连接校验***********
//        socketChannel.isOpen();	// 测试SocketChannel是 否为 open 状 态 
//        socketChannel.isConnected();	//测试SocketChannel是 否已 经 被连 接 
//        socketChannel.isConnectionPending();	//测试SocketChannel是否正在进行连接
//        socketChannel.finishConnect(); 	//校验正在进行套接字连接的SocketChannel是否已经完成连接 
        //************连接校验***********
    
        //************设置socket套接字的相关参数*******
        //通过setOptions方法可以设置socket套接字的相关参数
        socketChannel.getOption(StandardSocketOptions.SO_KEEPALIVE);// 保活连接
        Integer option = socketChannel.getOption(StandardSocketOptions.SO_RCVBUF);
        //************设置socket套接字的相关参数*******
        
        //读操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        //为阻塞式读时，当执行到read出，线程将阻塞，控制台将无法后续打印代码read over
        socketChannel.read(byteBuffer);
        socketChannel.close();
        System.out.println("read over");

    }

}
