package com.bigDragon.javase.ioStream.NIO;

import com.bigDragon.javase.ioStream.NIO.buffer.BufferDemo1;
import com.bigDragon.javase.ioStream.NIO.buffer.BufferDemo2;
import com.bigDragon.javase.ioStream.NIO.channel.*;
import com.bigDragon.javase.ioStream.NIO.selector.SelectorDemo1;
import com.bigDragon.javase.ioStream.NIO.selector.SelectorDemo2;
import com.bigDragon.javase.ioStream.NIO.selector.SelectorDemo3;

/**
 * @author: bigDragon
 * @create: 2025/5/14
 * @Description:
 */
public class NioMain {
    public static void main (String[] args) {
        //***************channel********************
        //FileChannel读取数据到Buffer示例Demo
        new FileChannelDemo1();
        //FileChannel写数据到Buffer示例Demo
        new FileChannelDemo2();
        //Channel.transferFrom 通道之间数据传输
        new FileChannelDemo3();
        //Channel.transferTo 通道之间数据传输
        new FileChannelDemo4();
        //ServerSocketChannel使用 建立非阻塞的SocketChannel通道
        new ServerSocketChannelDemo();
        //SocketChannel使用，作为客户端请求百度
        new SocketChannelDemo();
        //SocketChannel使用，客户端/服务端测试样例
        new SocketChannelDemo2();
        //ServerSocketChannel使用 accept接受消息
        new ServerSocketChannelDemo();
        //DatagramChannel UD网络传输
        new DatagramChannelDemo();
        //******************buffer**************************
        //buffer / IntBuffer 基础写入写出缓冲区
        new BufferDemo1();
        //缓冲区分片、只读缓冲区。直接缓冲区
        new BufferDemo2();
        //******************Selector**************************
        new SelectorDemo1();
        new SelectorDemo2();
        new SelectorDemo3();
    }
}
