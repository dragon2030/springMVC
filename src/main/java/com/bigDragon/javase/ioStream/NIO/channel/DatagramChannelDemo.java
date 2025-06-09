package com.bigDragon.javase.ioStream.NIO.channel;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

//Java NIO中UDP通信的基本模式，作为学习NIO DatagramChannel的入门示例
public class DatagramChannelDemo {

    //发送的实现
    @Test
    public void sendDatagram() throws Exception {
        //打开 DatagramChannel
        DatagramChannel sendChannel = DatagramChannel.open();
        InetSocketAddress sendAddress =
                new InetSocketAddress("127.0.0.1",9999);

        //发送
        while(true) {
            ByteBuffer buffer = ByteBuffer.wrap("发送atguigu".getBytes("UTF-8"));
            sendChannel.send(buffer,sendAddress);
            System.out.println("已经完成发送");
            Thread.sleep(1000);
        }
    }

    //接收的实现
    @Test
    public void receiveDatagram() throws Exception {
        //打开DatagramChannel
        DatagramChannel receiveChannel = DatagramChannel.open();
        InetSocketAddress receiveAddress = new InetSocketAddress(9999);
        //绑定 绑定通道到指定端口进行监听
        receiveChannel.bind(receiveAddress);

        //buffer
        ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

        //接收
        while(true) {
            receiveBuffer.clear();
            //这边没有configureBlocking，默认阻塞，receive会进行阻塞直到有消息传入
            SocketAddress socketAddress = receiveChannel.receive(receiveBuffer);

            receiveBuffer.flip();

            System.out.println(socketAddress.toString());
            //由于在传入中进行编码，那拿出时也需要进行解码，将字节解码为字符串
            System.out.println(Charset.forName("UTF-8").decode(receiveBuffer));
        }
    }
    //测试步骤 先启动发送端sendDatagram 再启动接受端receiveDatagram

    //连接  read  和 write
    @Test
    public void testConnect() throws Exception {
        //打开DatagramChannel
        DatagramChannel connChannel = DatagramChannel.open();
        //绑定
        connChannel.bind(new InetSocketAddress(9999));

        //连接
        connChannel.connect(new InetSocketAddress("127.0.0.1",9999));

        //write方法 通过write进行了发送
        connChannel.write(ByteBuffer.wrap("发送atguigu".getBytes("UTF-8")));

        //buffer
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        while(true) {

            readBuffer.clear();
            //通过read方法进行接收
            connChannel.read(readBuffer);

            readBuffer.flip();
            System.out.println(Charset.forName("UTF-8").decode(readBuffer));

        }
    }

}
