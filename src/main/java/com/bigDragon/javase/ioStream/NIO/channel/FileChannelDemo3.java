package com.bigDragon.javase.ioStream.NIO.channel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

//通道之间数据传输
public class FileChannelDemo3 {

    //transferFrom()
    public static void main(String[] args) throws Exception {
        // 创建两个fileChannel
        RandomAccessFile aFile = new RandomAccessFile("D:\\io_test\\testTxt\\nio_1.txt","rw");
        FileChannel fromChannel = aFile.getChannel();

        RandomAccessFile bFile = new RandomAccessFile("D:\\io_test\\testTxt\\nio_3.txt","rw");
        FileChannel toChannel = bFile.getChannel();

        //fromChannel 传输到 toChannel
        long position = 0;
        long size = fromChannel.size();
        toChannel.transferFrom(fromChannel,position,size);

        aFile.close();
        bFile.close();
        System.out.println("over!");
    }
}
