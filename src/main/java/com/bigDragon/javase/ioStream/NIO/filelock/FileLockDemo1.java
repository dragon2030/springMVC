package com.bigDragon.javase.ioStream.NIO.filelock;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLockDemo1 {

    //nio写文件
    @Test
    public void writeFile() throws Exception {
        String input = "atguigu";
        System.out.println("input:"+input);

        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());

        String filePath = "d:\\atguigu\\01.txt";
        Path path = Paths.get(filePath);
    
        // 打开文件通道，打开模式为WRITE和APPEND，表示追加写入
        FileChannel channel =
                FileChannel.open(path,
                        StandardOpenOption.WRITE,StandardOpenOption.APPEND);
        // 将位置设置为文件末尾前一个位置
        channel.position(channel.size());

        //加锁 排它锁
        FileLock lock = channel.lock();
        System.out.println("是否共享锁："+lock.isShared());

        channel.write(buffer);
        channel.close();
        lock.release();
    }
    
    //bio读文件
    @Test
    private void readFile() throws Exception {
        String filePath = "d:\\atguigu\\01.txt";
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String tr = bufferedReader.readLine();
        System.out.println("读取出内容：");
        while(tr != null) {
            System.out.println(" "+tr);
            tr = bufferedReader.readLine();
        }
        fileReader.close();
        bufferedReader.close();
    }
}
