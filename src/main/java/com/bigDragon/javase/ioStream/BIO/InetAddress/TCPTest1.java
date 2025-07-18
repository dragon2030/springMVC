package com.bigDragon.javase.ioStream.BIO.InetAddress;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现TCP的网络编程
 *
 * 例子1：客户端发送信息给服务端，服务端将数据显示在控制台上
 *
 * 客户端和服务端通信过程
 *
 * 在服务器端创建ServerSocket对象，并绑定监听端口。调用ServerSocket对象的accept()方法监听客户端的请求。与客户端建立连接后，它会返回一个已连接
 * 的Socket对象，并通过输入流读取客户端发送的请求信息，然后通过输出流向客户端发送响应信息，最后关闭socket及相关资源。
 *
 * 在客户端创建Socket对象，需要指定连接服务器的地址和端口号，和服务器建立连接后，通过输出流向服务端发送请求信息，然后通过输入流获取服务器的响应信息，
 * 最后关闭socket及相关资源。
 *
 * @author bigDragon
 * @create 2020-11-27 14:42
 *
 */
public class TCPTest1 {
    //客户端
    @Test
    public void client(){
        try {
            //1.创建Socket对象，指明服务器端ip和端口号
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            Socket socket = new Socket(inetAddress,8899);
            //2.获取一个输出流，用于输出数据
            OutputStream outputStream = socket.getOutputStream();
            //3.写出数据的操作
            outputStream.write("你好我是客户端mm".getBytes());
            System.out.println("数据推送成功");
            //资源的关闭释放
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //服务端
    @Test
    public void server(){
        try {
            //创建服务器端的ServerSocket，指明自己的端口号
            ServerSocket serverSocket=new ServerSocket(8899);
            //调用accept() 表示接受来自客户的socket
            Socket socket = serverSocket.accept();
            //获取输入流
            InputStream inputStream = socket.getInputStream();
            //读取输入流数据
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[5];
            int len;
            while ((len = inputStream.read(buffer)) !=-1){
                byteArrayOutputStream.write(buffer,0,len);
            }
            System.out.println(byteArrayOutputStream.toString());
            System.out.println("收到来来自于："+socket.getInetAddress().getHostAddress()+"的数据");
            //释放资源
            byteArrayOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
