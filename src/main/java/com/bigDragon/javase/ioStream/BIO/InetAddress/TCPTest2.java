package com.bigDragon.javase.ioStream.BIO.InetAddress;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现TCP的网络编程
 * 例题2：客户端发送文件给服务端，服务端将文件保存在本地
 *
 * @author bigDragon
 * @create 2020-11-27 15:41
 * 
 * 问题：客户端和服务端设置的缓存空间大小不一致，在数据流传输的时候是怎么进行的
 * Java中的流传输机制。当客户端和服务端通过Socket传输数据时，数据是通过操作系统内核的TCP缓冲区进行中转的。客户端写入OutputStream的数据会被发送
 *      到内核的发送缓冲区，然后通过网络传输到服务端的内核接收缓冲区，服务端再从接收缓冲区读取数据。这个过程与应用程序设置的缓冲区大小无关，因为内核
 *      有自己的缓冲区管理。
 * 
 */
public class TCPTest2 {
    @Test
    public void client(){
        try {

            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            Socket socket = new Socket(inetAddress,8899);
            OutputStream outputStream = socket.getOutputStream();

            FileInputStream fileInputStream=new FileInputStream(new File("src\\Main\\resources\\jpg\\0509-01.jpg"));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }

            System.out.println("数据推送成功");
            //资源的关闭释放
            outputStream.close();
            socket.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            FileOutputStream fileOutputStream = new FileOutputStream(new File("src\\Main\\resources\\jpg\\0509-06.jpg"));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) !=-1){
                fileOutputStream.write(buffer,0,len);
            }
            System.out.println("收到来来自于："+socket.getInetAddress().getHostAddress()+"的数据");
            //释放资源
            fileOutputStream.close();
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
