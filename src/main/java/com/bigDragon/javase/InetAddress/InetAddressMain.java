package com.bigDragon.javase.InetAddress;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 一、网络编程中有两个主要的问题：
 *      1.如何准确地定位网络上一台或多台主机：定位主机上的特定的应用
 *      2.找到主机后如何可靠高效地进行数据传输
 *
 * 二、网络编程中的两个要素：
 *      1.对应问题一：IP和端口号
 *      2.对应问题二：提供网络通信协议。TCP/IP参考模型（应用层、传输层、网络层、物理+数据链路层）
 *
 *  三、通讯要素一、IP和端口号
 *      1.IP：唯一的标识Internet上的计算机（通讯实体）
 *      2.在Java中使用InetAddress类代表IP
 *      3.IP分类：IPV4 和 IPV6 ; 万维网 和 局域网
 *      4.域名：www.baidu.com
 *      5.本地回路地址：127.0.0.1 对应着：localhost
 *      6.如何实例化InetAddress:两个方法getByName(String host)、getlocalHost()
 *          两个常用方法：getHostName()/getHostAddress()
 *      7、端口号：正在计算机上运行的进程。
 *              要求：不同进程有不同的端口号
 *              范围：被规定为一个16位的整数0~65535
 *      8.端口号与IP地址的组合得出一个网络套接字：Socket
 *
 * @author bigDragon
 * @create 2020-11-26 15:23
 */
public class InetAddressMain {
    public static void main(String[] args){
        InetAddressMain inetAddressMain = new InetAddressMain();
        //InetAddress类的常用方法
        inetAddressMain.test1();

        //实现TCP的网络编程
        //例子1：客户端发送信息给服务端，服务端将数据显示在控制台上
        new TCPTest1();


    }
    @Test
    public void test1(){
        try {
            //IP
            InetAddress inetAddress = InetAddress.getByName("192.168.10.14");
            System.out.println(inetAddress);
            //域名
            InetAddress inetAddress2 = InetAddress.getByName("www.atguigu.com");
            System.out.println(inetAddress2);

            InetAddress inetAddress3 = InetAddress.getByName("127.0.0.1");
            System.out.println(inetAddress3);
            //本机地址
            InetAddress inetAddress4 = InetAddress.getLocalHost();
            System.out.println(inetAddress4);

            //getHostName()
            System.out.println(inetAddress2.getHostName());
            //getHostAddress()
            System.out.println(inetAddress2.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
