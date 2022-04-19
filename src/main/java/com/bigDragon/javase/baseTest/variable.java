package com.bigDragon.javase.baseTest;

import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author bigDragon
 * @create 2021-03-08 19:48
 */

public class variable {
    @Test
    public void test1(){
        //byte范围：-128 ~ 127
        byte b1 = 127;
        //声明为long型变量，必须以“l”或“L”结尾
        short s1 = 32767;
        int i1 = 2147483647;
        long l1 = 9223372036854775807L;

        //定义float类型变量时，变量要以“f”或“F”结尾
        //通常定义浮点变量时，使用double型
        double d1  = 123.3;
        float f1 = 234.5f;
        int i = "中国是".codePointAt(1);

        //1.定义为char型变量，通常使用一对''
        char c1= 'a';
        System.out.println((byte)c1);
        //2.申明一个转移字符
        char c2= '\n';//输出一个换行符
        //3.直接使用Unicode值来表示字符型常量
        char c3='\u0043';
        System.out.println(c3);//C
    }
    //基本数据类型间运算
    @Test
    public void test2(){
        //自动类型提升
        byte b1 = 2;
        int i1 = 129;
        int i2 = b1 + i1;
        float f = b1 + i1;
        //System.out.println(f);//131.0

        char c1 = 'a';//97
        byte b2 = 1;
        short s2 = 128;
        int i3=1;
        int i = c1 + i3;
        int i4 = c1 + b2;
        int i5 = b2 + s2;
        int i6 = b2 + b2;//同种类型间

        //强制类型转换
        //精度损失举例1
        double d1 = 12.9;
        int d2 = (int) d1;//截断操作
        System.out.println(d2);//12

        //没有精度损失
        long l1 = 128;
        short s3 = (short)l1;

        //精度损失举例2
        int i7 = 128;
        byte b = (byte)i7;
        System.out.println(b);//-128

    }
    @Test
    public void test3(){
        char c= 'a';
        int num = 10;
        String str = "hello";
        System.out.println(str+num+c);//hello10a
    }
    @Test
    public void test4(){
        //二进制
        int i = 0b101;
        System.out.println(i);//5
        //八进制
        int i2 = 012;
        System.out.println(i2);//10
        //十六进制
        int i3 = 0x12;
        System.out.println(i3);//18

        //-127的补码为1000 0001，-128的补码为1000 0000
        byte b = 127;
        byte b2 = (byte)(b+1);
        System.out.println(b2);//-128

    }
    @Test
    public void test5(){
        //十进制 --> 二进制
        String s = Integer.toBinaryString(20);
        System.out.println(s);//10100
        //十进制 --> 八进制
        String s1 = Integer.toOctalString(20);
        System.out.println(s1);//24
        //十进制 --> 十六进制
        String s2 = Integer.toHexString(20);
        System.out.println(s2);//14
        //二进制 --> 十进制
        int i1 = Integer.parseInt("10100", 2);
        System.out.println(i1);//20
        //八进制 --> 十进制
        int i2 = Integer.parseInt("24", 8);
        System.out.println(i2);//20
        //十六进制 --> 十进制
        int i3 = Integer.parseInt("14", 16);
        System.out.println(i3);//20
    }
    @Test
    public void test6(){
        //除号
        int num1 = 12;
        int num2 = 5;
        System.out.println(num1 / (num2 + 0.0));//2.4
        System.out.println((double)num1 / num2 );//2.4
        //取余运算
        int m1 = 12;
        int m2 = -12;
        int n1 = 5;
        System.out.println(m1 % n1);//2
        System.out.println(m2 % n1);//-2
        //结果的符合与被模数的符合相同
        int i1 = 10;
        int i = i1++;
        System.out.println(i);
    }
    @Test
    public void test7(){
        //赋值符合：=
        int i1=10;
        int i2=20;
        int i3=30;
        i3=i2=i1;
        System.out.println(i1);//10
        System.out.println(i2);//10
        System.out.println(i3);//10
        //支持连续赋值

        //开发中，如果希望变量实现+2操作
        //方式一：num = num + 2
        //方式二：num += 2；(推荐)
        byte num = 0;
        num += 2;
        System.out.println(num);//2 结论：不会改变变量本身的数据类型
    }
    @Test
    public void test8(){
        System.out.println("hello" instanceof String);//true

        boolean b1 = true;
        boolean b2 = false;
        System.out.println(b1=b2);//显示b1的值
        int i=100;
        System.out.println(i=3);//3
    }
    @Test
    public void test9(){
        //区别& 与&&
        //相同点1：&和&&运算结果相同
        //相同点2：当符合左边是true时，二者都会执行符合左边的运算
        //不同点：当符号左边是false时，&继续执行右边的运算，&&不再执行右边的运算
        boolean b1 = true;
        boolean b2 = false;
        int i1 = 10;
        int i2 = 10;
        System.out.println(b1 & (i1++>0));//true
        System.out.println(i1);//11
        System.out.println(b1 && (i1++>0));//true
        System.out.println(i1);//12

        System.out.println(b2 & (i2++>0));//false
        System.out.println(i2);//11
        System.out.println(b2 && (i2++>0));//false
        System.out.println(i2);//11 未执行++操作
    }
    @Test
    public void test10(){
        int i = 21;
        System.out.println("i << 2: "+(i << 2));//21*4
        System.out.println("i << 27: "+(i << 27));//-1476395008 解释：此时最高位为1
        System.out.println("i >> 2: "+(i >> 2));//21/4=5
        int num1 = 5;
        int num2 = 7;
        System.out.println(num1^num2^num2);
    }
    //从控制台输入相关变量：Scanner类
    @Test
    public void test11(){
        //Scanner的实例化方法：new Scanner(System.in)
        Scanner scanner = new Scanner(System.in);
        //调用Scanner类的相关方法（next()/nextXXX），用来获取指定类型的变量
        int i = scanner.nextInt();
        System.out.println("int型:"+i);
        String s = scanner.next();
        System.out.println("String型："+s);
        float f = scanner.nextFloat();
        System.out.println("float型："+f);
    }

    @Test
    public void test12(){
/*        if(1==2)
            System.out.println(1);
        System.out.println(2);*/
        if(1==1)
            if(3==2)
                System.out.println("3==2");
            else
                System.out.println("3!=2");//3!=2
    }
    //switch-when
    @Test
    public void test13(){
        //不写break则继续执行default
        int i = 3;
        switch (i){
            case 1:
                System.out.println(1);
                break;
            case 2:
                System.out.println(2);
                break;
            case 3:
                System.out.println(3);
                break;
            default:
                System.out.println("default");
        }
    }
    @Test
    public void test14(){
        int score = 77;
        switch (score/10){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                System.out.println("不及格");
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                System.out.println("及格");
        }
    }
    //for循环
    @Test
    public void test15(){
        //for循环
        String st = "123";
        for(int i=0;i<st.length();i++){
            System.out.println(st.charAt(i));
        }
        //for增强
        for(char c:"123".toCharArray()){
            System.out.println(c);
        }

    }
    //while循环
    @Test
    public void test16(){
        //初始化条件
        int i = 0;
        while(true){//循环条件
            System.out.println(++i);//循环体
            if(i>5){break;}//迭代条件
        }
    }
    //do-while
    @Test
    public void test17(){
        int i=0;//初始化条件
        do{
            System.out.println(++i);//循环条件
            if(i>5){break;}//迭代条件
        }while(true);//循环条件
    }
    @Test
    public void test18(){
        lable:for(int i=0;i<10;i++){
            System.out.println("   i:"+i);
            for(int j=0;j<10;j++){
                System.out.println("j:"+j);
                if(i==5)
                    continue lable;
            }
        }
    }
    public void test19(){
        byte b1 = -128;
        short s1 = 32767;
        short s2 = -32768;
        int i1 = 2147483647;
        System.out.println(String.valueOf(i1).length());//10
        int i2 = -2147483648;
        long l1 = 9223372036854775807L;
        long l2 = -9223372036854775808L;
        System.out.println(String.valueOf(l1).length());//19
    }
    //变量的值传递
    @Test
    public void test20(){
        int m = 20;
        int n = m;
        n = 1;
        System.out.println(m+"-"+n);//20-1
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = arr1;
        arr2[0] = 4;
        System.out.println(Arrays.toString(arr1)+"-"+Arrays.toString(arr2));//[4, 2, 3]-[4, 2, 3]
    }
    //单精度，双进度的有效值
    @Test
    public void test21(){
        //单精度
        float f = 523456789123f;//9位有效数字？
        System.out.println(f);
        //双精度
        double d = 123456789123456789D;//18位有效数字？
        System.out.println(d);//1.23456789123456784E17
    }
    //可变形参
    public void method1(int ... i){
        System.out.println("method1");
    }
    public void method1(int i){
        System.out.println("method2");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.method1(1);//method2 原因:method1不确定个数，method2确定个数，确定个数的有限调用
    }
}
