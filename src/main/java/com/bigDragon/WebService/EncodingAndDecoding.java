/**
 * 
 */
package com.bigDragon.WebService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author: bigDragon
 * @date: 2020年8月14日
 * 
 */
public class EncodingAndDecoding {
	public String encoding(String st,String parseCharset){
		//如果传递的字符串中包含非西欧字符的字符串，会被转化成%XX%XX  XX为十六进制的数字
		String urlString="";
		try {
			urlString = URLEncoder.encode("你好", parseCharset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return urlString;
	}
	public String decoding(String st,String parseCharset){
		//将上面加码后的字符串进行解码
		String keyWord="";
		try {
			keyWord = URLDecoder.decode(st, parseCharset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return keyWord;
	}
	public String encoding2(String st,String charsetName,String parseCharset) {
		String string=null;
		try {
			byte[] bytes = st.getBytes(charsetName);
			string = new String(bytes,parseCharset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}
	

	public static void main(String[] args){
		EncodingAndDecoding encodingAndDecoding = new EncodingAndDecoding();
		String st="你好";
		String charsetName="UTF-8";
		String parseCharset="GBK";
		String encoding = encodingAndDecoding.encoding(st,parseCharset);
		System.out.println(encoding);//%C4%E3%BA%C3
		String decoding = encodingAndDecoding.decoding(encoding,parseCharset);
		System.out.println(decoding);//你好
		String encoding2 = encodingAndDecoding.encoding2(st, charsetName,parseCharset);
		System.out.println(encoding2);

	}
}
