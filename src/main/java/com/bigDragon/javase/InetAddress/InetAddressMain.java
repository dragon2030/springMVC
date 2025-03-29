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
 *         要求：不同进程有不同的端口号
 *         范围：被规定为一个16位的整数0~65535
 *      8.端口号与IP地址的组合得出一个网络套接字：Socket
 *
 * @author bigDragon
 * @create 2020-11-26 15:23
 * 
 * 问题一：是通过java创建了一个进程占用了指定端口，接收数据提供服务嘛 
 * 问题一的答案应该是肯定的，服务端通过ServerSocket占用了指定端口，用于接收数据提供服务。不过需要注意的是，这里可能是在同一个进程内的两个测试方法，
 *  如果分别运行的话，可能作为不同进程。但通常@Test方法可能按顺序运行，可能不会同时启动，所以实际使用中可能需要分开部署。
 *
 * 问题二：这种传输方式是同步阻塞的嘛，服务端和客户端在哪一行代码会阻塞
 * Java的Socket API默认是同步阻塞的。也就是说，当调用accept()、read()等方法时，线程会阻塞直到有数据可处理。在服务端代码中，
 *      serverSocket.accept()这一行会阻塞，直到有客户端连接进来。同样，在客户端连接到服务器后，服务端的inputStream.read(buffer)也会阻塞，
 *      直到有数据可读或者流结束。而客户端在Socket的构造函数（new Socket(...)）时，会尝试连接到服务器，这一过程可能会阻塞直到连接建立或超时。
 *      不过在这个例子中，客户端可能很快连接成功，所以可能不明显。输出流写入数据时，如果缓冲区已满，也可能阻塞，但这里发送的数据量小，可能不会阻塞。
 *（注意）如何避免阻塞？
 * 可通过多线程（每个连接一个线程）或 NIO（Selector 非阻塞模型）优化，但示例中是基础的同步阻塞实现。
 *
 * 问题：是通过什么机制让进程阻塞等待，直到客户端/服务端响应（深入知道阻塞底层是怎么实现）
 * 问题可以分为两部分：一是Java中的阻塞方法如何工作，二是操作系统层面的机制如何支持这种阻塞。同时需要解释系统调用、内核态和用户态的切换，以及线程状态的变化。
 * 首先，Java的Socket API是基于操作系统提供的系统调用的。例如，ServerSocket的accept()方法对应了操作系统的accept()系统调用，
 *      而InputStream的read()对应了read()系统调用。当这些系统调用被执行时，如果数据未准备好，操作系统会将当前线程置于阻塞状态，并调度其他线程运行。
 * 然后，需要解释线程阻塞的具体过程。当线程执行到阻塞方法时，会从用户态切换到内核态，内核会检查是否有数据可用（如新的连接或可读的数据）。如果没有，
 *      内核将线程标记为等待状态，并放入等待队列，直到相应的事件发生（如新连接到达或数据到达）。此时，线程不会占用CPU资源，直到事件触发后，内核将其唤醒，
 *      线程重新进入就绪队列，等待CPU调度。
 * 
 * 问题：一个服务器端提供服务时是不是只能对应一个客户端，其他的客户端会进去等待
 * 但在当前的代码中，服务端处理完一个客户端后，并没有循环调用accept()，假设服务端持续运行，比如在循环中调用accept()，那么每个accept()会处理一个
 *      客户端连接。但如果没有多线程，服务端在处理当前客户端时，无法同时接受其他客户端的连接，其他客户端必须等待当前处理完成。因此，在单线程的情况下，
 *      服务器确实是顺序处理每个客户端，其他客户端需要等待。
 */
public class InetAddressMain {
    public static void main(String[] args){
        InetAddressMain inetAddressMain = new InetAddressMain();
        //InetAddress类的常用方法
        inetAddressMain.test1();

        //实现TCP的网络编程
        //例子1：客户端发送信息给服务端，服务端将数据显示在控制台上
        new com.bigDragon.javase.InetAddress.TCPTest1();
        //例题2：客户端发送文件给服务端，服务端将文件保存在本地
        new com.bigDragon.javase.InetAddress.TCPTest2();
        //例题3：从客户端发送文件给服务端，服务端保存到本地，并返回“发送成功”给客户端。
        new com.bigDragon.javase.InetAddress.TCPTest3();
        
        //nio适用场景建议：
        //小文件/低频场景：BIO 代码简单，易于维护。
        //大文件/高并发场景：必须使用 NIO 或更高级框架（如 Netty）。
        
        //NIO
        new com.bigDragon.javase.InetAddress.NioClient();
        new com.bigDragon.javase.InetAddress.NioServer();
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
