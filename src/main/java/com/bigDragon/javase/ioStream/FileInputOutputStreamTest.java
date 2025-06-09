/**
 *
 */
package com.bigDragon.javase.ioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件的写入写出字节流
 * 测试FileInputStream和FileOutputStream的使用
 *
 * 结论：
 * 1.对于文本文件(.txt,.java,.c,.cpp)，使用字符流处理
 * 2.对于非文本文件(.jpg,.mp3,.mp4,.avi,.doc)，使用字节流处理
 *
 * @author: bigDragon
 * @date: 2020年8月26日
 *
 */
public class FileInputOutputStreamTest {

	public static void main(String[] args){
		FileInputOutputStreamTest fStreamTest=new FileInputOutputStreamTest();
		//使用字节流处理文本文件
		fStreamTest.testFileInputStream();
		//非文本文件的输入输出（复制）
//		fStreamTest.testFileInputStreamFileOutputStream();
	}

	/**
	 * 使用字节流处理文本文件
	 * 注：可能出现乱码的情况
	 * 例：Hello World!!��国���
	 */
	public void testFileInputStream(){
		//造流
		FileInputStream fStream = null;
		try {
			//造文件
			File file = new File("src\\Main\\resources\\file\\hello.txt");//文本文件
			fStream = new FileInputStream(file);
			//读数据
			byte[] buffer = new byte[5];//开发中byte数组长度一般取1024
			int len;//记录每次读取字节的个数
			while ((len = fStream.read(buffer)) != -1) {
//				for(byte b : buffer){
//					System.out.print(b+" ");
//				}
				String str = new String(buffer, 0, len);
				System.out.println(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//关闭资源
				if (fStream != null)
					fStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * 非文本文件的输入输出（复制）
	 * @throws IOException
	 */
	public void testFileInputStreamFileOutputStream(){
		//2.创建输入流和输出流的对象
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;

		try {
			//1.创建File类的对象，指明读入和写出的文件
			File srcfile = new File("src\\Main\\resources\\jpg\\0509-01.jpg");//图像文件
			File destfile = new File("src\\Main\\resources\\jpg\\0509-02.jpg");//图像文件
			//File srcfile = new File("src\\Main\\resources\\mp3\\Laser Blast.mp3");//音频文件
			//File destfile = new File("src\\Main\\resources\\mp3\\Laser Blast2.mp3");//音频文件
			fileInputStream = new FileInputStream(srcfile);
			fileOutputStream = new FileOutputStream(destfile);
			//3.数据的读入和写出操作
			byte[] buffer = new byte[1024];//开发中byte数组长度一般取1024
			int len;//记录每次读入到buffer数组中字符的个数
			while ((len = fileInputStream.read(buffer)) != -1) {
				//测试使用在控制台打印byte
//				for(byte b:buffer){
//					System.out.print(b+" ");
//				}
//				String str = new String(buffer, 0, len);
//				System.out.println(str);
				//每次写入len个字节
				fileOutputStream.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//4，关闭资源
				fileInputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				fileOutputStream.close(); // close()会自动调用flush()
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
