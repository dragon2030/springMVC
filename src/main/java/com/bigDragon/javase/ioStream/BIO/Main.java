/**
 *
 */
package com.bigDragon.javase.ioStream.BIO;

import com.bigDragon.javase.ioStream.NIO.Nio;
import com.bigDragon.javase.ioStream.excel.ExcelSimpleDemo;

/**
 * I/O原理及使用
 * @author: bigDragon
 * @date: 2020年8月21日
 *
 */
public class Main {
/*
	I/O是Input/Output的缩写，I/O技术用于处理设备之间的数据传输。
	Java程序中，对于数据的输入/输出操作以"流(stream)"的方式进行
	java.io包下提供了各种“流”类和接口，用以获取不同种类的数据，并通过标准的方法输入或输出数据。

	一、流的分类：
	按操作数据单位不同分为：字节流（8 bit）字符流（16 bit）
	按照数据流的流向不同分为：输入流，输出流
	按流的角色的不同分为：节点流，处理流

	输入流中 字节流InputStream 字符流Reader
	输出流中 字节流OutputStream 字符流Writer
	Java中IO流共涉及40多个类，都是从4个抽象基类派生。
	由这四个类派生处理的子类名称都以其父类名作为子类名的后缀

	二、流的体系结构(主要必学)
	抽象基类				节点流（或文件流）			缓冲流（处理流的一种） 		转换流（处理流的一种）		对象流（处理流的一种）
	InputStream			FileInputStream			BufferedInputStream								ObjectInputStream
	OutputStream		FileOutputStream		BufferedOutputStream							ObjectOutputStream
	Reader				FileReader				BufferedReader			InputStreamReader
	Writer				FileWriter				BufferedWriter			OutputStreamWriter

	需做需补练习与知识点：
	2.RandomAccessFileTest断点续传功能与MD5验证文件功能
	3.文件上传下载实例（NIO传输，io流源码理解与不同io方式比对，传输过程的监听，el表达式，jstl）

	java文件上传和下载 jsp和java代码实现文件上传下载 io nio
	https://www.cnblogs.com/liubin1988/p/8003848.html

 */
	public static void main(String[] args){
		//File类的使用和理解
		FileTest.main(args);

		//节点流（或文件流）	
		//文件的写入写出 字符流
		FileReader_FileWriter.main(args);
		//文件的写入写出字节流
		FileInputOutputStreamTest.main(args);

		//缓冲流（处理流的一种） 	
		BufferedTest.main(args);
		//图片加密解密
		PicTest.main(args);

		//处理流之二：转换流的使用
		InputStreamReaderTest.main(args);

		//编码与解码测试类
		EncoderDecode.main(args);

		//处理流之三：输入输出流
		//处理流之四：打印流
		//处理流之五：数据流
		OtherStream.main(args);
		//处理流之六：对象流
		ObjectInputOutputStreamTest.main(args);
		//处理流之七：随机处理对象流
		RandomAccessFileTest.main(args);
		//Java NIO NOIO2
		Nio.main(args);
		//excel文件的读写
		ExcelSimpleDemo.main(args);

		//文件上传
		new uploadDemo();
		//管道流
		new PipedStreamTest();
		//测试练习
	}


}
