package com.bigDragon.javase.ioStream.NIO.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class PipeDemo {

    public static void main(String[] args) throws IOException {
        //**************读数据************
        //1 获取管道
        Pipe pipe = Pipe.open();

        //2 获取sink通道
        Pipe.SinkChannel sinkChannel = pipe.sink();

        //3 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("atguigu".getBytes());
        byteBuffer.flip();

        //4 写入数据
        sinkChannel.write(byteBuffer);

        //**************读数据************
        //5 获取source通道
        Pipe.SourceChannel sourceChannel = pipe.source();

        //6 读取数据
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);

        int length = sourceChannel.read(byteBuffer2);

        System.out.println(new String(byteBuffer2.array(),0,length));

        //7 关闭通道
        sourceChannel.close();
        sinkChannel.close();
    }

}
