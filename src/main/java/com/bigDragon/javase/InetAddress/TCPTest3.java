package com.bigDragon.javase.InetAddress;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
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
 * 例题3：从客户端发送文件给服务端，服务端保存到本地，并返回“发送成功”给客户端。
 *
 * @author bigDragon
 * @create 2020-11-27 16:53
 */
public class TCPTest3 {
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
            //关闭数据的输出，不再发送数据，使服务端阻塞的方法中止
            socket.shutdownOutput();
            System.out.println("数据推送成功");

            //接收来自服务端的数据并显示到控制台上
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer2 = new byte[1024];
            int len2 = 0;
            while ((len2 = inputStream.read(buffer2)) != -1){
                byteArrayOutputStream.write(buffer2,0,len2);
            }
            System.out.println("服务端返回数据"+byteArrayOutputStream.toString());

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
            FileOutputStream fileOutputStream = new FileOutputStream(new File("src\\Main\\resources\\jpg\\0509-07.jpg"));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) !=-1){//inputStream.read为一个阻塞性的方法
                fileOutputStream.write(buffer,0,len);
            }
            System.out.println("收到来来自于："+socket.getInetAddress().getHostAddress()+"的数据");
            //服务端给以客户端反馈
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("已收到文件".getBytes());

            //释放资源
            fileOutputStream.close();
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
