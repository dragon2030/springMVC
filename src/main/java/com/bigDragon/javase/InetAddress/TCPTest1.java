package com.bigDragon.javase.InetAddress;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

import java.io.ByteArrayInputStream;
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
 * @author bigDragon
 * @create 2020-11-27 14:42
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
