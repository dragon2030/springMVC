/**
 * 
 */
package com.bigDragon.javase.ioStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * 日志文件log输入类
 * 
 * @author: bigDragon
 * @date: 2020年9月1日
 * 
 */
public class LogImport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogImport logImport = new LogImport();
		logImport.test1("C:\\Users\\3759\\Desktop\\日志\\need import\\123.log");
	}

	public void test1(String srcPath) {
		InputStreamReader isr = null; 
		BufferedReader br = null;
		FileInputStream fis = null;
		try {
			File file = new File(srcPath);
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			br = new BufferedReader(isr);
			String data;
			while ((data = br.readLine()) != null) {
				System.out.println(data);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
				isr.close();
				fis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
