/**
 * 
 */
package com.bigDragon.javase.ioStream.BIO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

import com.bigDragon.regular.RegularExpression;

/**
 * 其他流的使用
 * 1.标准输入、输出流
 * 2.打印流
 * 3.数据流
 * 
 * 
 * 处理流之三、标准的输入、输出流
 * 
 * 1.1
 * System.in:标准的输入流，默认从键盘输入
 * System.out: 标准的输出流，默认从控制台输出
 * 
 * 1.2
 * Systeml类的setIn(InputStream is)/setOut(PrintStram out)方式重新指定输入和输出的流。
 * 
 * 1.3练习
 * 从键盘输入字符串，要求将读取到的整行字符串转成大写输入。然后继续进行输入操作，
 * 直到当输入“e”或者“exit”时，退出程序
 * 
 * 方法一：使用Scanner实现，调用next返回一个字符串									otherStream.ScannerTest3()
 * 方法二：使用System.in实现，System.in ---> 转换流 ---> BufferedReader的readLine		
 * 
 * 2. 处理流之四：打印流：PritStream和PrintWriter
 * 2.1 PritStream和PrintWriter提供了一系列重载的print和println方法，用于多种数据类型的输出
 * 2.2 PritStream和PrintWriter有自动flush功能
 * 2.3 PrintStream打印的所有字符都使用平台默认字符编码转换为字节。
 * 在需要写入字符而不是字节的情况下，应该使用PrintWriter类。
 * 2.4 System.out返回的是PrintStream的实例
 * 
 * 3. 处理流之五：数据流
 * 指 DataInputStream 和 DataOutputStream，用于按基本数据类型（如 int、double、String）读写二进制数据。
 * 特点：
 * 		直接操作原始数据类型（如 writeInt(int)、readDouble()）。
 * 		数据以紧凑的二进制格式存储，适合高效读写。
 * 		不涉及对象序列化，仅处理简单数据。
 * 3.1 对象流和数据流 核心区别与选择依据
 * 特性			数据流（Data Streams）				对象流（Object Streams）
 * 处理数据类型	基本类型（int, double, String）	对象（需实现 Serializable 接口）
 * 数据格式		紧凑的二进制，无类型元信息			包含类元数据，支持对象结构重建
 * 性能			更高（轻量级，直接操作原始数据）		较低（涉及序列化/反序列化开销）
 * 灵活性		需手动管理数据类型顺序和格式			自动处理对象关联和类型信息
 * 安全性		无序列化漏洞风险					需防范反序列化攻击（如恶意对象注入）
 * 典型场景		配置文件、简单数据传输				对象持久化、跨进程/网络传输复杂数据	
 * 3.2 对象流和数据流 如何选择？
 * 使用数据流：
 * 		处理简单的基本类型数据。
 * 		需要高性能或紧凑的二进制格式。
 * 		无需保存对象关联关系。
 * 使用对象流：
 * 		需要保存或传输整个对象及其关联结构。
 * 		数据包含复杂类型（如集合、嵌套对象）。
 * 		接受序列化带来的性能和安全性代价。
 * 3.3 数据流的限制
 * 必须严格匹配读写顺序：读取数据的顺序必须与写入顺序完全一致，否则会抛出异常。	
 * 
 */
public class OtherStream {

	public static void main(String[] args) {
		OtherStream otherStream=new OtherStream();
		//Scanner.next方法
		otherStream.ScannerTest1();
		//Scanner.nextLine方法
		otherStream.ScannerTest2();
		//控制台输入方法一 输出exit时候退出
		otherStream.ScannerTest3();
		//控制台输入方法二 控制台输入 转换流 缓冲流 控制台输出
		otherStream.consolePrintOut();
		//打印流 PritStream和PrintWriter
		otherStream.printStream();
		//数据流
		//数据流：输出
		otherStream.DataStream1("src\\Main\\resources\\file\\hello5.txt");
		otherStream.DataStream2("src\\Main\\resources\\file\\hello5.txt");
	}
	
	/**
	 * Scanner.next方法
	 * next():只读取输入直到空格。它不能读两个由空格或符号隔开的单词。此外，next()在读取输入后将光标放在同一行中。(next()只读空格之前的数据,并且光标指向本行)
	 */
	public void ScannerTest1(){
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
 
        // next方式接收字符串
        System.out.println("next方式接收：");
        // 判断是否还有输入
        if (scan.hasNext()) {
            String str1 = scan.next();
            System.out.println("输入的数据为：" + str1);
        }
        scan.close();
	}
	/**
	 * Scanner.next方法
	 * nextLine():读取输入，包括单词之间的空格和除回车以外的所有符号(即。它读到行尾)。读取输入后，nextLine()将光标定位在下一行。
	 */
	public void ScannerTest2(){
		Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
 
        // nextLine方式接收字符串
        System.out.println("nextLine方式接收：");
        // 判断是否还有输入
        if (scan.hasNextLine()) {
            String str2 = scan.nextLine();
            System.out.println("输入的数据为：" + str2);
        }
        scan.close();
	}

	/**
	 * 控制台输入方法一
	 * 使用Scanner实现
	 */
	public void ScannerTest3(){
		Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
 
        // nextLine方式接收字符串
        System.out.println("nextLine方式接收：");
        // 判断是否还有输入
        while (scan.hasNextLine()) {
            String str2 = scan.nextLine();
            System.out.println("输入的数据为：" + str2.toUpperCase());
            RegularExpression regularExpression = new RegularExpression();
            if(regularExpression.match(".*e.*", str2) || regularExpression.match(".*exit.*", str2)){
            	break;
            }
        }
        scan.close();
	}
	
	/**
	 * 控制台输入方法二
	 * 使用System.in实现
	 * 流转换
	 * 
	 * @throws IOException 
	 */
	public void consolePrintOut(){
		InputStreamReader isr;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(System.in);
			br = new BufferedReader(isr);
			while (true) {
				String data = br.readLine();
				RegularExpression regularExpression = new RegularExpression();
				if (regularExpression.match(".*e.*", data) || regularExpression.match(".*exit.*", data)) {
					break;
				}
				System.out.println(data.toUpperCase());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br != null)
					br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 打印流
	 * PritStream和PrintWriter
	 * 
	 * @throws FileNotFoundException 
	 */
	public void printStream(){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("src\\Main\\resources\\file\\hello6.txt"));
			//创建打印输出流，设置为自动刷新模式（写入换行符或字节'\n'时都会刷新输出缓存区）
			PrintStream ps = new PrintStream(fos, true);
			if (ps != null) {// 把标准输出流（控制台输出）改成文件
				System.setOut(ps);
			}
			for (int i = 0; i <= 255; i++) {//输出ASCII字符
				System.out.print((char) i);
				if (i % 50 == 0) {//每50个数据一行
					System.out.println();//换行
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fos != null){
					fos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 数据流：输出
	 */
	public void DataStream1(String outPrintPath){
		DataOutputStream dos = null;
		
		try {
			dos = new DataOutputStream(new FileOutputStream(outPrintPath));
			dos.writeUTF("Jack");
			dos.flush();//刷新操作，将内存中数据写入页面
			dos.writeInt(23);
			dos.flush();
			dos.writeBoolean(true);
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(dos != null)
					dos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 数据流：输出
	 * 将文件中存储的基本类型数据变量和字符串读取到内存中，保存在变量
	 * 注意点：不同类型的数据的顺序要与当初写入文件时，保存的数据顺序一致
	 */
	public void DataStream2(String inputPath){
		DataInputStream dis = null;
		
		try {
			dis = new DataInputStream(new FileInputStream(inputPath));
			String name = dis.readUTF();
			int age = dis.readInt();
			Boolean isMale = dis.readBoolean();
			System.out.println("name:" + name);
			System.out.println("age:" + age);
			System.out.println("isMale:" + isMale);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(dis != null)
					dis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}	
		}
	}
}
