package com.bigDragon.thinkOfJava.ThinkOfJavaChapterTwo.practive1;

import java.math.BigDecimal;
import java.util.Calendar;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Practice1 practice1=new Practice1();
/*		System.out.println("int初始值："+practice1.i);
		System.out.println("char初始值："+practice1.c);
		//解析char初始值
		int parseInt=Integer.valueOf(practice1.c);
		System.out.println("int:"+parseInt);
		String parseString=String.valueOf(practice1.c);
		System.out.println("st:"+parseString);*/
		
/*		System.out.println("byte初始值："+practice1.b);
		System.out.println("boolean初始值："+practice1.bool);
		System.out.println("float初始值："+practice1.f);
		System.out.println("double初始值："+practice1.d);
		System.out.println("long初始值："+practice1.l);
		System.out.println("short初始值："+practice1.s);*/
		
		//包装类的相互装换
		byte b=1;
		Byte byte1=Byte.valueOf(b);
		Byte byte2=new Byte(b);
		Byte byte3=b;
		byte b1=byte1.byteValue();
		byte b2=byte1;
		
		char ch='\u039A';
		Character character=Character.valueOf(ch);
		Character character2=new Character('A');
		Character character3=ch;
		char char1=character.charValue();
		char char2=character2;
		System.out.println("character2："+(int)char2);
		
		short sh=12;
		Short short1=Short.valueOf(sh);
		Short short2=new Short(sh);
		Short short3=sh;
		short sh1=short1.shortValue();
		short sh2=short1;
		
		
		
		Integer integer=Integer.valueOf(practice1.i);
		Integer integer2=new Integer(practice1.i);
		Integer integer3=practice1.i;
		int int1=integer.intValue();
		int int2=integer;
		System.out.println("int："+int2);
		
		float f=1.1f;
		Float float1=Float.valueOf(f);
		Float float2=new Float(f);
		Float float3=f;
		float f1=float1.floatValue();
		float f2=float3;
		
		double d=1;
		Double double1=Double.valueOf(d);
		Double double2=new Double(d);
		Double double3=d;
		double d1=double1.doubleValue();
		double d2=double1;
		
		boolean bool=true;
		Boolean boolean1=Boolean.valueOf(bool);
		Boolean boolean2=new Boolean(bool);
		Boolean boolean3=bool;
		boolean bool1=boolean1.booleanValue();
		boolean bool2=boolean1;
		Calendar.getInstance();
		Math.random();
		BigDecimal bigDecimal=BigDecimal.ZERO;
		System.out.println(bigDecimal);
		BigDecimal bigDecimal2=new BigDecimal("0");
		System.out.println(bigDecimal2);
		
	}
	public static void test1(){
		System.out.println("测试方法输出值");
	}
}
