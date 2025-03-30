/**
 * 
 */
package com.bigDragon.javase.ioStream;

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
 * BufferedWriter
 * 
 * 2.作用：提供流的读取、写入的速度
 * 		提高读写速度的原因：内部提供了一个缓存区
 * 
 * 3.处理流：就是“套接”在已有的流的基础上 
 * 
 * 工作原理：
 * 缓冲流的工作原理是将数据先写入缓冲区中，当缓冲区满时再一次性写入文件或输出流，或者当缓冲区为空时一次性从文件或输入流中读取一定量的数据。这样可以减少系统的 I/O 操作次数，提高系统的 I/O 效率，从而提高程序的运行效率。
 * https://blog.csdn.net/zch981964/article/details/130759889?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522c38b8e69472c31f5db4aa384fea4a170%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=c38b8e69472c31f5db4aa384fea4a170&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-1-130759889-null-null.142^v102^pc_search_result_base4&utm_term=java%20%E7%BC%93%E5%86%B2%E6%B5%81&spm=1018.2226.3001.4187
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
		//实现非文本文件的复制
		bufferedTest.bufferedInputStreamTest("src\\Main\\resources\\jpg\\0509-01.jpg",
				"src\\Main\\resources\\jpg\\0509-02.jpg");
		//使用BufferedReader和BufferedWriter实现文本文件的复制（重点 这个在生产中用到的非常多）
		bufferedTest.testBufferedReaderBufferWriter("src\\Main\\resources\\file\\hello.txt",
				"src\\Main\\resources\\file\\hello4.txt");
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
