package com.bigDragon.javase.baseTest;

import com.bigDragon.javase.faseToObject.interfasce.B;
import org.junit.Test;

import java.util.HashMap;

/**
 * 包装类
 * @author bigDragon
 * @create 2021-04-09 10:34
 */
public class WrapperTest {
    //基本数据类型 --> 包装类：调用包装类的构造器
    @Test
    public void test1(){
        int i = 10;
        Integer integer = new Integer(i);
        System.out.println(integer);//10
        System.out.println(integer.toString());//10
        Integer integer1 = new Integer("10");
        System.out.println(integer1);//10
        //Integer integer2 = new Integer("123abc");//异常：java.lang.NumberFormatException: For input string: "123abc"

        Float aFloat = new Float(12.3f);
        Float aFloat1 = new Float("12.3");
        System.out.println(aFloat);//12.3
        System.out.println(aFloat1);//12.3

        Boolean aBoolean = new Boolean(true);
        Boolean aTrue = new Boolean("true");
        Boolean aTrue2 = new Boolean("true2");
        System.out.println(aBoolean);//true
        System.out.println(aTrue);//true
        System.out.println(aTrue2);//false //特殊的：不会报异常，进行过优化 return ((s != null) && s.equalsIgnoreCase("true"));
        test3(new Integer(1));

        int i2 = 1;
        Integer integer2 = i2;
        System.out.println(integer2);//1
    }
    //包装类-->基本数据类型：调用包装类的xxxValue()
    @Test
    public void test2(){
        Integer integer = new Integer(12);
        int i = integer.intValue();
        System.out.println(i);//12
        System.out.println(i+1);//13

        Float aFloat = new Float(12.3);
        float v = aFloat.floatValue();
        System.out.println(v);//12.3
        Integer integer2 = new Integer(2);
        int i2 = integer2;
        System.out.println(i2);//2
    }
    public void test3(Object o){
        System.out.println(o);
    }
    //基本数据类型、包装类-->String类型：调用String重载的valueOf(Xxx xxx)
    @Test
    public void test4(){
        int num1 = 10;
        //方式1：连接运算
        String str1 = num1+"";
        System.out.println(str1);//"10"
        //方式2：调用String重载的valueOf(Xxx xxx)
        String str2 = String.valueOf(10);
        System.out.println(str2);//"10"
        String s = String.valueOf(new Float(12.3));
        System.out.println(s);//12.3
    }
    //String类型-->基本数据类型、包装类:使用包装类的parseXxx()
    @Test
    public void test5(){
        String str1 = "123a";
        int i = Integer.parseInt(str1);
        System.out.println(i+1);//124

        String str2 = "123a";
        int i2 = Integer.parseInt(str2);//NumberFormatException

        String str3 = "true1";
        boolean b = Boolean.parseBoolean(str3);
        System.out.println(b);//false
    }
    @Test
    public void test6(){
        //Integer内部定义了IntegerCache结构，IntegerCache中定义了Integer[]，
        //保存了从-128~127范围的整数，如具有我们使用自动装箱的方式，给Integer赋值的范围在
        //-128~127范围内时，直接使用数组中的元素，不用再去new了。目的，提高效率
        Integer m = 1;
        Integer n = 1;
        System.out.println(m == n);//true

        Integer x = 128;//相当于new了一个Integer对象
        Integer y = 128;//相当于new了一个Integer对象
        System.out.println(x == y);//false
    }
    @Test
    public void test7(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("1",1);
        Object o = map.get("1");
        Integer integer = (Integer)o;
        int i = integer;
        System.out.println(i);//1
    }
    @Test
    public void test8(){
        Object o = 1;
        Integer integer = (Integer)o;
        int i = integer;
        System.out.println(i);//1
    }
    @Test
    public void test9(){
        Object o = 1;
        int i = (int)o;
        System.out.println(i);//1
    }
    //jdk5.0之前
    @Test
    public void test10(){
        Object obj = 1;
        Integer integer = (Integer) obj;
        int i = integer.intValue();
        System.out.println(i);//1
    }
    @Test
    public void test11(){
        boolean b = true;
        Object obj = b;
        System.out.println(obj instanceof Boolean);
    }


    

    public static void main(String[] args) {
        Object o1 = true ? new Integer(1) : new Double(2.0);
        System.out.println(o1);//1.0
    }
}
