/**
 * 
 */
package com.bigDragon.javase.ioStream.BIO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件的写入写出字符流
 * 测试FileReader和FileWriter的使用
 * 
 * 注意：
 * 	1.文件流的操作必须用try{}catch(){}finally,用抛出异常的方式会导致打开的打开的流对象未关闭造成资源浪费。
 * 	2.不能使用字符流来处理图片等字节数据
 * 
 * @author: bigDragon
 * @date: 2020年8月25日
 * 
 */
public class FileReader_FileWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

			FileReader_FileWriter f = new FileReader_FileWriter();
			//获取资源文件hello.txt的file对象
			f.getFile();
			
			//将file文件中hello.txt输出到控制台
			f.testFileReader();
			//对read()操作升级：使用read的重载方法read(char cbuf[])
			f.testFileReader2();
		
			//从内存中写出数据到硬盘文件
			f.testFileWriter();
			
			//文件流的输入输出（复制）
			f.testFileReaderFileWriter();


	}
	/**
	 * 获取资源文件hello.txt的file对象
	 */
	public void getFile(){
		File file = new File("src\\Main\\resources\\file\\hello.txt");//相较于当前工程
		System.out.println(file.getAbsolutePath());//获取其绝对路径
	}
	/**
	 * 将file文件中hello.txt输出到控制台
	 * 
	 * 说明点：
	 * 1.read():返回读入的一个字符。如果达到文件末尾，返回-1
	 * 2.异常的处理：为了保证流资源一定可以执行关闭操作。需要使用try-catch-finally
	 * 3.读入的文件一定要存在，否则就会报FileNotFoundException。
	 * 
	 * @throws IOException 
	 */
	public void testFileReader(){
		//提供具体的类
		FileReader fileReader = null;
		try {
			//实例化File类的对象，指明要操作的文件
			File file = new File("src\\Main\\resources\\file\\hello.txt");
			fileReader = new FileReader(file);
			//数据的读入
			//read():返回读入的一个字符。如果达到文件末尾，返回-1
			//方式一：
			//		int read = fileReader.read();
			//		while(read != -1){
			//			System.out.print((char)read);
			//			read = fileReader.read();
			//		}
			//方式二：语法上针对方式一的修改
			int read;
			//将整型强制类型转换为字符型，JVM 会把数字当成字符的 ASCII 编码来处理。例如字符 '(' 的 ASCII 编码为 40，
			//	所以将整型 40 强制类型转换为字符型，会得到字符 '('。
			while ((read = fileReader.read()) != -1) {
				System.out.print(read+": ");
				System.out.println((char) read);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//4.流的关闭操作
			try {
				//判断FileReader对象是否实例化，或者创建时就出错
				if(fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//对read()操作升级：使用read的重载方法
	public void testFileReader2() {
		//2.FillReader流的实例化
		FileReader fr = null;
		try {
			//1.File类的实例化
			File file = new File("src\\Main\\resources\\file\\hello.txt");
			fr = new FileReader(file);
			//3.读入的操作
			//read(char[] cbuffer):返回每次度日cbuffer数组中的字符个数
			char[] cbuffer = new char[5];//开发中char数组长度一般取1024??? 如何指定不同三个字节的字符进入两个字节的char
			int len;
			while ((len = fr.read(cbuffer)) != -1) {
				//方式一：
/*				for (int i = 0; i < len; i++) {
					System.out.print(cbuffer[i]);
				}*/
				//方式二：
				String str=new String(cbuffer,0,len);
				System.out.print(str);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//4.资源的关闭
				if(fr != null)
					fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}


	}
	
	/**
	 * 从内存中写出数据到硬盘文件
	 * 
	 * 说明
	 * 1.输出操作：对应File可以不存在
	 * 2.
	 * 	File对应的硬盘中的文件如果不存在，在输出的过程中，会自动创建此文件
	 * 	File对应的硬盘中的文件如果存在：
	 * 		如果流使用的构造器是：FileWriter(file,false)/FileWriter(file)对原有文件的替换
	 * 		如果流使用的构造器是：FileWriter(file,true)对原有文件的追加
	 * @throws IOException 
	 */
	public void testFileWriter() {
		//2.提供FileWriter的对象，用于数据的写出
		//FileWriter fileWriter=new FileWriter(file);//默认对原有人家的替换
		//FileWriter fileWriter=new FileWriter(file,false);//对原有文件的替换
		FileWriter fileWriter = null;//对原有文件的追加
		try {
			//1.提供File类的对象，指明写出到的文件
			File file = new File("src\\Main\\resources\\file\\hello2.txt");
			fileWriter = new FileWriter(file, true);
			//3.写出的操作
			fileWriter.write("I have a dream\n");
			fileWriter.write("!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//4.流资源关闭
			try {
				if(fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 文件流的输入输出（复制）
	 * @throws IOException 
	 */
	public void testFileReaderFileWriter(){
		//2.创建输入流和输出流的对象
		FileReader fileReader = null;
		FileWriter fileWriter = null;
		
		try {
			//1.创建File类的对象，指明读入和写出的文件
			File srcfile = new File("src\\Main\\resources\\file\\hello.txt");
			File destfile = new File("src\\Main\\resources\\file\\hello3.txt");
			fileReader = new FileReader(srcfile);
			fileWriter = new FileWriter(destfile);
			//3.数据的读入和写出操作
			char[] cbuf = new char[5];//开发中char数组长度一般取1024
			int len;//记录每次读入到cbuf数组中字符的个数
			while ((len = fileReader.read(cbuf)) != -1) {
				//测试使用在控制台打印byte
				for(char c:cbuf){
					System.out.print(c+" ");
					System.out.println((int)c);
				}
				//每次写入len个字符
				fileWriter.write(cbuf, 0, len);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//4，关闭资源
				fileWriter.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				fileReader.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}
