package com.bigDragon.javase.ioStream.NIO.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//FileChanne写操作
public class FileChannelDemo2 {

    public static void main(String[] args) throws Exception {
        // 打开FileChannel
        RandomAccessFile aFile = new RandomAccessFile("D:\\io_test\\testTxt\\nio_2.txt","rw");
        FileChannel channel = aFile.getChannel();

        //创建buffer对象
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        //写入内容
        buffer.put("data atguigu".getBytes());

        buffer.flip();

        //FileChannel完成最终实现
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }

        //关闭
        channel.close();
    }
}
