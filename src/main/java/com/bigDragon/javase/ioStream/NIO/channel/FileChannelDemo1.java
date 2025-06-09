package com.bigDragon.javase.ioStream.NIO.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 关键点总结
 1. 通道(Channel)：连接数据源和数据目标的管道
 2. 缓冲区(Buffer)：数据临时存储区，是NIO的核心
 3. 缓冲区状态转换：
 * flip()：写→读
 * clear()：读→写
 4. 非阻塞I/O：虽然这个例子是阻塞式的，但NIO主要优势在于非阻塞I/O
 这个例子展示了NIO的基本使用模式，实际应用中可能会使用更高效的方式处理数据，比如直接操作缓冲区而不是逐个字节读取。
 */
public class FileChannelDemo1 {
    //FileChannel读取数据到buffer中 演示简单demo
    public static void main(String[] args) throws Exception {
        /**
         *  创建FileChannel
         *  FileChannel需要通过文件进行创建，通过类RandomAccessFile
         *  使用RandomAccessFile以读写模式("rw")打开指定路径的文件
         *  RandomAccessFile既可以读也可以写文件，并且可以随机访问文件位置
         */
        RandomAccessFile aFile = new RandomAccessFile("D:\\io_test\\testTxt\\nio_1.txt","rw");
        //从RandomAccessFile对象获取FileChannel实例,FileChannel是NIO中用于文件操作的通道，支持高效的I/O操作
        FileChannel channel = aFile.getChannel();
    
        /**
         * 创建Buffer
         * 分配一个1024字节的ByteBuffer缓冲区
         * 缓冲区是NIO的核心，用于临时存储数据
         */
        ByteBuffer buf = ByteBuffer.allocate(1024);
    
        /**
         * 读取数据到buffer中
         * 从Channel读取数据到Buffer
         * 从通道读取数据到缓冲区，返回实际读取的字节数
         * 如果到达文件末尾，返回-1
         */

        int bytesRead = channel.read(buf);
        while(bytesRead != -1) {
            System.out.println("读取了："+bytesRead);
            //flip()：切换缓冲区模式，从写模式(刚读取数据)转为读模式(准备处理数据)
            buf.flip();
            //纯ASCII文本
            while(buf.hasRemaining()) {//是否有剩余的内容
                //buf.get()只是从ByteBuffer中读取单个字节（返回byte类型，强制转换为char），并不涉及字符编码解码
                //(char)buf.get()的写法适用于纯ASCII文本
                //多字节字符（如中文UTF-8） Charset.decode(ByteBuffer)
                System.out.print((char)buf.get());
            }
            //多字节字符（如中文UTF-8）
//            Charset charset = Charset.forName("UTF-8");
//            CharBuffer charBuffer = charset.decode(buf);
//            System.out.print(charBuffer.toString());
            //清空缓冲区，准备下一次读取。同时缓冲区状态转换 读→写
            //可读数据已读完,但底层字节数组中的数据依然存在（只是被标记为不可读）,需要调用clear重置【深入学习】
            buf.clear();
            bytesRead = channel.read(buf);//为下一遍循环
        }
        aFile.close();
        System.out.println("结束了");
    }
}
