/**
 * 
 */
package com.bigDragon.javase.ioStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 图片加密解密
 * 
 * @author: bigDragon
 * @date: 2020年8月27日
 * 
 * 问题：异或运算就能实现简单加密，再异或运算就能返回图片本身 原理
 * 假设有一个字节的数据是A，用密钥B进行异或，得到A^B。然后再用B异或一次的话，(A^B)^B就等于A^(B^B)，而B^B的结果是0，所以A^0就是A本身。
 * 		所以两次异或同一个密钥就能恢复原始数据。
 */
public class PicTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PicTest picTest=new PicTest();
		//加密
		picTest.test1("src\\Main\\resources\\jpg\\0509-01.jpg",
				"src\\Main\\resources\\jpg\\0509-03.jpg");
		//解密
		picTest.test1("src\\Main\\resources\\jpg\\0509-03.jpg",
				"src\\Main\\resources\\jpg\\0509-04.jpg");
	}

	public void test1(String srcStr,String destStr){
		FileInputStream fiStream = null;
		FileOutputStream foStream = null;
			
		try {
			fiStream = new FileInputStream(srcStr);
			foStream = new FileOutputStream(destStr);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fiStream.read(buffer)) != -1) {
				//字节数组进行修改
				for (int i = 0; i < len; i++) {
					buffer[i] = (byte) (buffer[i] ^ 5);
				}
				foStream.write(buffer, 0, len);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				foStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				fiStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}
