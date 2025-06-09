package com.bigDragon.javase.ioStream.NIO.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
//*************监听直到第一个请求******
public class SelectorDemo1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        //创建selecotr
        Selector selector = Selector.open();
        //通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //非阻塞
        serverSocketChannel.configureBlocking(false);
        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //将通道注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 阻塞直到至少一个通道就绪，需要在一个循环中调用 select()，然后检查 selectedKeys
            int readyChannels = selector.select();
            System.out.println("****************selector.select***********"+readyChannels);
            //查询已经就绪通道操作
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //判断key就绪状态操作
                if (key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    System.out.println("isAcceptable");
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                    System.out.println("isConnectable");
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                    System.out.println("isReadable");
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                    System.out.println("isWritable");
                }
                iterator.remove();
                Thread.sleep(1000);
            }
            //****************hasNext***********
            //isAcceptable
        }
    }
    
//    }
}
