package com.bigDragon.ioStream;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 随机存取文件流
 * RandomAccessFile的使用
 * 1.RandomAccessFile直接继承与java.lang.Object类，实现了DataInput和DataOutput接口
 * 2.RandomAccessFile既可以作为一个输入流，又可以作为一个输出流
 * 3.如果RandomAccessFile作为输出流时，写出到的文件如果不存在，则在执行过程中创建
 *   如果写出到文件存在，则会对原有文件内容进行覆盖。（默认情况下重头覆盖）
 * 4.可以通过相关的操作，实现RandomAccessFile“插入”数据的效果
 *
 * 练习：
 * 用RandomAccessFile实现视频断点传输功能
 *
 * @author bigDragon
 * @create 2020-09-03 20:26
 */
public class RandomAccessFileTest {
    public static void main(String[] args){
        RandomAccessFileTest raf= new RandomAccessFileTest();
        //RandomAccessFile对文件的复制
        //raf.test("src\\main\\resources\\jpg\\0509-01.jpg","src\\main\\resources\\jpg\\0509-05.jpg");
        //RandomAccessFile文件的写入
        //raf.test2("src\\main\\resources\\file\\hello6.txt");
        //RandomAccessFile文件的“插入”数据
        raf.test3("src\\main\\resources\\file\\hello6.txt");
    }

    /**
     * RandomAccessFile对文件的复制
     * @param srcPath
     * @param outputPath
     */
    public void test(String srcPath,String outputPath){
        RandomAccessFile raf1 = null;
        RandomAccessFile raf2 = null;
        try{
            raf1 = new RandomAccessFile(srcPath,"r");
            raf2 = new RandomAccessFile(outputPath,"rw");

            byte[] buffer = new byte[1024];
            int len;
            while((len = raf1.read(buffer)) != -1){
                raf2.write(buffer,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(raf1 != null)
                raf1.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }
            try{
                if(raf1 != null)
                    raf2.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * RandomAccessFile文件的写入（覆盖或者追加的效果）
     * @param srcPath
     */
    public void test2(String srcPath){
        RandomAccessFile raf1 = null;
        try {
            raf1 = new RandomAccessFile(srcPath,"rw");

            //将文件记录指针定位到pos位置
            //raf1.seek(3);
            raf1.seek(new File(srcPath).length());//插入数据导文件末尾

            raf1.write("23231312".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(raf1 != null)
                    raf1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * RandomAccessFile文件的“插入”数据
     * 将指针后的数据复制到变量中，调整指针，插入数据完成后再将变量后的数据追加回目标文件
     */
    public void test3(String srcPath) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(srcPath,"rw");

            raf.seek(3);//将指针调到角标为3的位置
            //保存指针3后面的所有数据导StringBuilder中
            //也可将StringBuilder替换为ByteArrayOutputStream
            StringBuilder builder = new StringBuilder((int)new File(srcPath).length());
            byte[] buffer = new byte[1024];
            int len;
            while((len = raf.read(buffer)) != -1){
                builder.append(new String(buffer,0,len));
            }
            //调回指针，写入数据
            raf.seek(3);
            raf.write("xyz".getBytes());

            //将StringBuilder中的数据写入到文件中
            raf.write(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
