package com.bigDragon.javase.ioStream;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * java NIO知识
 * 一.java NIO概述
 *
 * Java NIO（New IO，Non-Blocking IO）是从Java1.4版本开始映入的一套行的
 * IO API，可以替换标准的Java IO API。NIO与原本的IO有相同的作用和目的，
 * 但是使用起来的方式完全不同，NIO支持面向缓冲区的（IO是面向流的）、基于通
 * 道的IO操作。NIO将以更加高效的方式进行文件的读写操作
 *
 * Java API中提供了两套NIO，一套是针对标准输入输出的NIO，另一套就是网络编程NIO
 *
 * 二、NIO.2中Path、Path2、FileS类的使用
 * 早期的Java只提供了一个File类来访问文件系统。但File类的功能比较有限，所提供的
 * 方法性能也不高。而且，大多数方法在出错时仅返回失败，并不会提供异常信息。
 * NIO.2为了弥补这种不足，引入了Path接口，代表一个平台无关的平台路径，描述了目录
 * 结构中文件的位置。Path可以看成File类的升级版本，实际引用的资源也可以不存在
 *
 * @author bigDragon
 * @create 2020-09-08 11:36
 */
public class Nio {
    public static void main(String[] args){
        Nio nio = new Nio();
        //测试Path类使用
        nio.testPath();
        //测试Files类使用
//        nio.testFiles();
    
        //适用场景建议：
        //小文件/低频场景：BIO 代码简单，易于维护。
        //大文件/高并发场景：必须使用 NIO 或更高级框架（如 Netty）。
    
        //NIO
        new com.bigDragon.javase.InetAddress.NioClient();
        new com.bigDragon.javase.InetAddress.NioServer();
    }

    /**
     * 测试Path类使用
     */
    public void testPath(){
        
        Path path = Paths.get("src\\Main\\resources\\file\\hello.txt");
        System.out.println(path);
        //判断是否以path路径开始
        System.out.println(path.startsWith("src\\"));
        //Path与File
        File file = path.toFile();//Path--->File
        Path newPath = file.toPath();//file--->Path
    }


}
