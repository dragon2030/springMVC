/**
 * 
 */
package com.bigDragon.javase.ioStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.bigDragon.model.User;

/**
 *  4、处理流之六：对象流
 *  4.1 ObjectInputStream和ObjectOutputStream
 *  4.2 用于存储和读取基本数据类型数据或对象的处理流。它的强大之处就是可以把Java中的对象写入到数据源中，也能把对象从数据源中还原回来
 *  4.3 序列化与反序列化
 *  	序列化：用ObjectOutputStream类保存基本类型数据或对象的机制
 *  	反序列化：用ObjectInputStream类读取基本类型数据或对象的机制
 *  	注意：ObjectInputStream和ObjectOutputStream不能序列化static和transient
 *  修饰的成员变量
 *  4.4 序列化和反序列化的过程
 *  序列化的过程:将内存中的java对象保存到磁盘中或通过网络传输出去---使用ObjectOutputStream实现反序列化过程：将磁盘文件中的对象还原为内存中的一个java对象--使用ObjectInputStream
 *  4.5 序列化对象的要求
 *  	如果需要放某个对象支持序列化机制，则必须让对象锁属的类及其属性是可序列化的，为了让某个类是可序列化的。
 *  	1) 该类必须实现如下两个接口之一。
 *  	Serializable
 *  	Externalizable （不常用）
 *  	2) 类提供一个全局常量：serialVersionUID 区别于别的序列化对象，必加。
 *  	如果没有显示定义这个静态变量，它的值是Java运行时环境根据类的内部细节自动生成的，当类的实例变量做了修改，serialVersionUID可能发生变化。
 * 		Java的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致的。
 * 		3) 除了当前Person类需要实现Serializable接口之外，还必须保证其内部所有属性也必须是可序列化的。（默认情况下，基本数据类型可序列化）
 * 4.6 序列化的机制：
 * 		对象序列化机制允许把内存中的Java对象转换成平台无关的二进制流，从而允许把这种二进制流持久的保存在磁盘上，或通过网络将这种二进制流传输到另一个网络节点，当其他程序获取到这种二进制
 * 	流，就可以恢复成原来的Java对象
 *
 * @author: bigDragon
 * @date: 2020年9月3日
 * 
 */
public class ObjectInputOutputStreamTest {

	public static void main(String[] args){
		ObjectInputOutputStreamTest objectInputOutputStreamTest = new ObjectInputOutputStreamTest();
		//序列化的过程:将内存中的java对象保存到磁盘中或通过网络传输出去---使用ObjectOutputStream实现
		//objectInputOutputStreamTest.testObjectOutputStream("src\\main\\resources\\file\\object.dat");
		objectInputOutputStreamTest.testObjectOutputStream("src\\main\\resources\\file\\object2.dat");
		//反序列化：将磁盘文件中的对象还原为内存中的一个java对象--使用ObjectInputStream
		objectInputOutputStreamTest.testObjectInputStream("src\\main\\resources\\file\\object2.dat");
	}
	
	/**
	 * 序列化的过程:将内存中的java对象保存到磁盘中或通过网络传输出去---使用ObjectOutputStream实现
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void testObjectOutputStream(String outputPath) {
		//造流造对象
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(outputPath));
			//数据写入
			//oos.writeObject(new String("金卡智能"));
			User user = new User();
			user.setName("Jack");
			user.setAge("25");
			user.setPeopleDes("very nice");
			user.setSexId("1");
			oos.writeObject(user);
			
			oos.flush();//刷新操作
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//关闭流对象
				if(oos != null)
					oos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 反序列化过程：将磁盘文件中的对象还原为内存中的一个java对象--使用ObjectInputStream
	 * 
	 * @param importPath
	 */
	public void testObjectInputStream(String importPath) {
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream(importPath));
			Object obj = ois.readObject();
			//String str = (String) obj;
			User user = (User)obj;
			System.out.println(user.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(ois != null)
					ois.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
