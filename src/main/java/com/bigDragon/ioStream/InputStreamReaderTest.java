/**
 * 
 */
package com.bigDragon.ioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 处理流之二：转换流的使用
 * 1.转换流
 * 		InputStreamReader：将一个字节的输入流转换为字符的输入流
 * 		OutputStreamWriter：将一个字符的输出流转换为字节的输出流
 * 2.作用：提供字节流与字符流之间的转换
 * 
 * 3.解码：字节、字节数组 ---->字符数组、字符串
 * 	 编码：字符数组、字符串----> 字节、字节数组
 * 
 * 4.字符集
 * ASCII：美国标准信息交换码。用一个字节的7位可以表示,0(0000 0000)-127(0111 1111)共128位.
 * ISO8859-1：拉丁码表，欧洲码表，用一个字节的8位表示。
 * GB2312:中国的中文编码表，最多两个字符编码所有字符（中文用两个字节英文用一个字节）
 * GBK：中国的中文编发表升级，融合更多的中国文字符号，简体繁体都有，最多两个字节编码（中文用两个字节英文用一个字节）
 * Unicode：国际标准码，融合了目前人类使用的所有字符。为每个字符分配唯一的字符码，所有的文字都用两个字节表示。
 * 		UTF-8:变长的字符集，通常用1-4个字节表示一个字符
 * ANSI：ANSI编码，美国国家标准协会,通常指的是平台的默认编码，例如英文操作系统中是ISO8859-1,中午系统是GBK
 * Unicode字符集只是定义了字符的集合和唯一编号。Unicode编码，则是对UTF-8,UTF-16等具体编码方案的统称而已，并不是具体的编码方案
 * 
 * 
 * @author: bigDragon
 * @date: 2020年8月28日
 * 
 */
public class InputStreamReaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputStreamReaderTest iTest = new InputStreamReaderTest();
		iTest.characterPrint("src\\main\\resources\\file\\hello.txt");
		//iTest.characterConvert("src\\main\\resources\\file\\hello.txt",
		//		"src\\main\\resources\\file\\hello5.txt");
	}
	
	public void characterConvert(String srcStr,String destStr){
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		
		try {
			//1.创建文件、创建流
			File file1 = new File(srcStr);
			File file2 = new File(destStr);
			FileInputStream fis = new FileInputStream(file1);
			FileOutputStream fos = new FileOutputStream(file2);
			isr = new InputStreamReader(fis, "utf-8");
			osw = new OutputStreamWriter(fos, "gbk");
			//2.读写过程
			char[] cbuf = new char[20];
			int len;
			while ((len = isr.read(cbuf)) != -1) {
				osw.write(cbuf, 0, len);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//3.关闭资源
			try {
				if(isr != null)
					isr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(osw != null)
					osw.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
	
	/**
	 * 使用转换流将文件写入控制台
	 * InputStreamReader的使用，实现字节的输入流到字符的输入流的转换
	 * @param srcStr
	 */
	public void characterPrint(String srcStr){
		InputStreamReader isr = null;
		try {
			FileInputStream fis = new FileInputStream(srcStr);
			//InputStreamReader isr = new InputStreamReader(fis);//使用系统默认的字符集
			//具体使用哪个字符集，取决于文件保存时使用的字符集
			isr = new InputStreamReader(fis, "utf-8");
			char[] cbuf = new char[20];
			int len;
			while ((len = isr.read(cbuf)) != -1) {
				String str = new String(cbuf, 0, len);
				System.out.println(str);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(isr != null)
					isr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
