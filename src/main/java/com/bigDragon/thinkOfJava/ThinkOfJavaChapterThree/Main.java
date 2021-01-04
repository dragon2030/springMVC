package com.bigDragon.thinkOfJava.ThinkOfJavaChapterThree;


public class Main {
	public class practice2{
		float f;
	}
	public static void main(String[] args){
		//内部类
		practice2 p=new Main().new practice2();
		p.f=new Float(2.2);
		
/*		long l=0XaaL;
		long l2=077L;
		System.out.println(l);
		System.out.println(l2);
		//二进制十进制转换
		String st=Long.toBinaryString(l);
		System.out.println(st);
		int i1=Integer.valueOf(st,2);
		System.out.println(i1);*/
		/*System.out.println(new Double("0").MAX_VALUE);
		System.out.println(new Double("0").MIN_VALUE);
		test20201209 main=new test20201209();
		main.practive13((char)34);*/
		Object object="";
		System.out.println(object=="");
	
	}
	public void practive13(char ch){
		System.out.println("char:"+ch);
		int i=(int)ch;
		System.out.println("int:"+i);
		String st=Integer.toBinaryString(i);
		System.out.println("st:"+st);
	}
}

