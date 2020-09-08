/**
 * 
 */
package com.bigDragon.ioStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 处理流之一：缓冲流的使用
 * 
 * 1.缓冲流：
 * BufferedInputStream
 * BufferedOutputStream
 * BufferedReader
 * BufferedWeiter
 * 
 * 2.作用：提供流的读取、写入的速度
 * 		提高读写速度的原因：内部提供了一个缓存区
 * 
 * 3.处理流：就是“套接”在已有的流的基础上 
 * 
 * 
 * @author: bigDragon
 * @date: 2020年8月26日
 * 
 */
public class BufferedTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedTest bufferedTest = new BufferedTest();
		bufferedTest.bufferedInputStreamTest("src\\main\\resources\\jpg\\0509-01.jpg",
				"src\\main\\resources\\jpg\\0509-02.jpg");
		bufferedTest.testBufferedReaderBufferWriter("src\\main\\resources\\file\\hello.txt",
				"src\\main\\resources\\file\\hello4.txt");
	}
	
	/**
	 * 实现非文本文件的复制
	 * @param srcStr 目标文件路径
	 * @param destStr 复制后的文件路径
	 */
	public void bufferedInputStreamTest(String srcStr,String destStr){
		//2.2造缓冲流
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		
		try {
			//1.造文件
			File srcfile = new File(srcStr);//图像文件
			File destfile = new File(destStr);//图像文件
			//2.造流
			//2.1造节点流
			FileInputStream fileInputStream = new FileInputStream(srcfile);
			FileOutputStream fileOutputStream = new FileOutputStream(destfile);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			//3.复制的细节：读取、写入
			byte[] buffer = new byte[10];//开发中byte数组长度一般取1024
			int len;
			while ((len = bufferedInputStream.read(buffer)) != -1) {
				bufferedOutputStream.write(buffer, 0, len);
				//bufferedOutputStream.flush();//手动刷新缓存区
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//4.关闭资源
			//要求：先关闭外层的流，再关闭内层的流
			try {
				if(bufferedInputStream != null)
					bufferedInputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(bufferedOutputStream != null)
					bufferedOutputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			//说明：在关闭外层流的同事，内层流也会自动进行关闭，我们可以省略内层流关闭
			//fileInputStream.close();
			//fileOutputStream.close();
		}

	}
	
	/**
	 * 使用BufferedReader和BufferedWriter实现文本文件的复制
	 * @param srcStr
	 * @param destStr
	 * @throws IOException 
	 */
	public void testBufferedReaderBufferWriter(String srcStr,String destStr){
		//创建文件和相应的流
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			br = new BufferedReader(new FileReader(new File(srcStr)));
			bw = new BufferedWriter(new FileWriter(new File(destStr)));
			//读写操作
			//方式一：使用char[]数组
/*			char[] cbuf = new char[1024];
			int len;
			while ((len = br.read(cbuf)) != -1) {
				bw.write(cbuf, 0, len);
			} */
			
			//方式二:使用String
			String data;
			while((data = br.readLine()) != null){
				//方法一
//				bw.write(data+"\n");//data中不包含换行符
				//方法二
				bw.write(data);//data中不包含换行符
				bw.newLine();//提供换行的操作
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(bw != null)
					bw.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(br != null)
					br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	

}
