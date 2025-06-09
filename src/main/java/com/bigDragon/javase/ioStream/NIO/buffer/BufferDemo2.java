package com.bigDragon.javase.ioStream.NIO.buffer;


import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo2 {

    static private final int start = 0;
    static private final int size = 1024;

    //内存映射文件io
     @Test
     public void b04() throws Exception {
         RandomAccessFile raf = new RandomAccessFile("d:\\atguigu\\01.txt", "rw");
         FileChannel fc = raf.getChannel();
         MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

         mbb.put(0, (byte) 97);
         mbb.put(1023, (byte) 122);
         raf.close();
     }
    //直接缓冲区
    @Test
    public void b03() throws Exception {
        String infile = "d:\\atguigu\\01.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel finChannel = fin.getChannel();

        String outfile = "d:\\atguigu\\02.txt";
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel foutChannel = fout.getChannel();

        //创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while (true) {
            buffer.clear();
            int r = finChannel.read(buffer);
            if(r == -1) {
                break;
            }
            buffer.flip();
            foutChannel.write(buffer);
        }
    }

    //只读缓冲区
    @Test
    public void b02() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }

        //创建只读缓冲区
        ByteBuffer readonly = buffer.asReadOnlyBuffer();

        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i);
            b *=10;
            buffer.put(i,b);
        }

        readonly.position(0);
        readonly.limit(buffer.capacity());

        while (readonly.remaining()>0) {
            System.out.print(readonly.get()+"\t");
        }
        //0	10	20	30	40	50	60	70	80	90
        //证明：如果原缓冲区的内容发生了变化，只读缓冲区的内容也随之发生变化
    }


    //缓冲区分片
    @Test
    public void b01() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }

        //创建子缓冲区
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();

        //改变子缓冲区内容
        for (int i = 0; i <slice.capacity() ; i++) {
            byte b = slice.get(i);
            b *=10;
            slice.put(i,b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());
    
//        while(buffer.hasRemaining()) {//检验buffer没有数据方法一
        while(buffer.remaining()>0) {//检验buffer没有数据方法二
            System.out.print(buffer.get()+"\t");
        }
        //0	1	2	30	40	50	60	7	8	9	
    }
}
