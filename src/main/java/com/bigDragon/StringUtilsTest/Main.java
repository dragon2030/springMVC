package com.bigDragon.StringUtilsTest;

import org.apache.commons.lang3.StringUtils;



/**
 * @author: bigDragon
 * @date: 2020年8月14日
 * 
 */
public class Main {
	public void test(String str){
		//这么多记是记不住了，保存起来，一边工作一边看， 熟能生巧。
		System.out.println("isEmpty: "+StringUtils.isEmpty(str));//是否为空，空格字符为false
		System.out.println("isNotEmpty: "+StringUtils.isNotEmpty(str));//是否为非空，空格字符为true
		
		System.out.println("isBlank: "+StringUtils.isBlank(str));//是否为空，空格字符为true
		System.out.println("isNotBlank: "+StringUtils.isNotBlank(str));//是否为非空，空格字符为false
	}
	
	public static void main(String[] args){
		Main main=new Main();
		main.test(" ");
	}
}
