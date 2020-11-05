/**
 * 
 */
package com.bigDragon.javase.ioStream;

import java.io.File;
import java.io.IOException;

/**
 * File类的使用和理解
 * @author: bigDragon
 * @date: 2020年8月21日
 * 
 */
public class FileTest {
	/*
	1.File类的构造器
		File(String filePath)
		File(String partentPath,String childPath)
		File(File parentFile,String childPath)
	2.路径的分类
		相对路径：相较于某个路径下，指明的路径
		绝对路径：包含盘符在内的文件或文件目录的路径
	3.路径分隔符
		windows和DOS系统默认使用“\”类表示
		UNIX和URL使用“/”来表示
	4.File类的常用方法：
	 */
	public static void main(String[] args){
		try {
			//File类 判断功能
			File file=new File("C:\\Users\\3759\\Desktop\\日志\\need import\\123.txt");
			//判断是否是文件目录
			System.out.println("判断是否是文件目录："+file.isDirectory());
			//判断是否是文件
			System.out.println("判断是否是文件："+file.isFile());
			//判断是否存在
			System.out.println("判断是否存在："+file.exists());
			//判断是否可选
			System.out.println("判断是否可选："+file.canRead());
			//判断是否可写
			System.out.println("判断是否可写："+file.canWrite());
			//判断是否隐藏
			System.out.println("判断是否隐藏："+file.isHidden());
			
			//File类 创建功能。
			//创建文件。若文件存在，测不创建，返回false
			System.out.println("文件创建功能："+file.createNewFile());
			//创建文件目录。如果此文件目录存在，不创建，如果此文件目录上层不存在，不创建
			System.out.println("创建文件目录功能："+file.mkdir());
			//创建文件目录。如果上层文件目录不存在，一并创建
			System.out.println("创建文件目录功能："+file.mkdirs());
			
			//File类的删除功能
			//删除文件或文件夹
			//System.out.println("删除文件或文件夹："+file.delete());
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
