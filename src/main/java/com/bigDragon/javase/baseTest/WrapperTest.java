package com.bigDragon.javase.baseTest;

import com.bigDragon.javase.faseToObject.interfasce.B;
import org.junit.Test;

import java.util.HashMap;

/**
 * 包装类
 */
public class WrapperTest {
    public static void main (String[] args) {
        //Integer缓存机制 源码解析
        new WrapperTest().integer_cash();
        //Integer缓存机制 很实用的一道题
        new WrapperTest().integer_cash_practice();
    }
    //Integer缓存机制 很实用的一道题
    @Test
    public void integer_cash_practice(){
        Integer integer = new Integer("100");
        Integer integer2 = 100;
        Integer integer3 = Integer.valueOf("100");
        System.out.println(integer==integer2);//false
        System.out.println(integer2==integer3);//true
        System.out.println(integer==100);//true
        System.out.println(integer2==100);//true
        Integer integer4 = Integer.valueOf("200");
        System.out.println(integer4==(Integer)200);//false
        /**
         Integer 缓存机制（-128 到 127）：
            new Integer("100") 会强制创建新对象，即使值在缓存范围内。
            integer2 = 100 【自动装箱Integer.valueOf】和 Integer.valueOf("100") 会从缓存中获取对象（不会新建）。
         拆箱比较：
            当 Integer 与 int（如 100）用 == 比较时，会自动拆箱为 int，比较数值 → true。
         特殊说明:
            若数值不在缓存范围（如 200），integer2 = 200 会创建新对象
         */
    }
    //Integer缓存机制 源码解析
    public void integer_cash(){
        Integer.valueOf(123);
        /**
         源码解析：如果 i 在 low（-128）和 high（默认 127）之间，则直接从缓存数组返回对象，否则新建 Integer 对象
         public static Integer valueOf(int i) {
         if (i >= IntegerCache.low && i <= IntegerCache.high)
         return IntegerCache.cache[i + (-IntegerCache.low)];
         return new Integer(i);
         }
         */
    }
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
    
}
