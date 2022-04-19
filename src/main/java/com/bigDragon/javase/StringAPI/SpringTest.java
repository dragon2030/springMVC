package com.bigDragon.javase.StringAPI;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Spring类
 *
 * @author bigDragon
 * @create 2020-12-25 16:10
 */
public class SpringTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        SpringTest springTest = new SpringTest();

        //String类
        //String的概念及不可变性
        springTest.test1();
        //String实例化方式
        springTest.test2();
        //String不同拼接方式的对比(内存中的特点)
        springTest.test3();
        //String的常用方法
        springTest.StringMethod();
        //涉及到String类与其他结构之前的转换
        springTest.StringConvert();

        //StringBuilder类 和 StringBuffer类
        springTest.StringBuilderStringBufferTest();
    }
    /*String的概念及不可变性
    String:字符串，使用一对""引起来表示
    1.String声明为 final的，不可被继承
    2.String实现了Serializable接口：表示字符串是支持序列化的
            实现了Comparable接口：表示String可以比较大小
    3.String内部定义了final char[] value用于存储字符串数据
    4.String：代表不可变的字符序列。简称：不可变性。
        体现:1.当对字符串重新赋值时，需要重写指定内存区域赋值，不能使用原有value进行赋值
            2.当对现有的字符串进行连接操作时，也需要重写指定内存区域赋值，不能使用原有value进行赋值
            3.当调用String的replace()修改字符或字符串时，也必须重写指定内存区域赋值，不能使用原有value进行赋值
    5.通过字面量的方式（区别于new）给一个字符串赋值，此时的字符串声明在字符串常量池（位于方法区）中
    6.字符串常量池（位于方法区）中是不会存储相同的字符串的
     */
    @Test
    public void test1(){
        String st1= "abc";
        String st2= "abc";
        System.out.println(st1==st2);
        String st3= "hello";
        st2=st3;
        System.out.println(st1);
        System.out.println(st2);

        String s1="abc";
        String s2="abc";
        s1+="def";
        System.out.println(s1);
        System.out.println(s2);

        String s3="abcd";
        String s4 = s3.replace("c", "e");
        System.out.println(s3);
        System.out.println(s4);
    }
    /*
    String实例化方式：
    方式一：通过字面量定义的方式
    方式二：通过new + 构造器的方式

        面试题：String s = new String("abc");方式创建对象，在内存中创建几个对象？
               两个：一个是堆空间中new结构，另一个是char[]对应的常量池的数据:"abc"
     */
    @Test
    public void test2(){
        //通过字面量定义的方式：此时的s1和s2的数据javaEE声明在方法区的字符串常量池中。
        String s1 = "javaEE";
        String s2 = "javaEE";


        //通过new + 构造器的方式：此时s3和s4保存的地址值，是数据在堆空间中开辟空间以后对应的地址值。
        String s3 = new String("javaEE");
        String s4 = new String("javaEE");

        //this.value = "".value;
        String s5 = new String();
        //    public String(String original) {
        //        this.value = original.value;
        //        this.hash = original.hash;
        //    }
        String s6 = new String("");

        String s7 = new String(new char[]{65,66});
        System.out.println(s7);

        System.out.println(s1==s2);//true
        System.out.println(s1==s3);//false
        System.out.println(s3==s4);//false
        System.out.println("*************************");
        Person person1 = new Person("Mike", 22);
        Person person2 = new Person("Mike", 22);
        System.out.println(person1.getName() == person2.getName());//true 字面量赋值的方式，比较的是常量池的地址
        System.out.println(person1.getAge() == person2.getAge());
    }
    /*
        String不同拼接方式的对比(内存中的特点)
            结论
            >常量与常量的拼接结果在常量池。且常量池中不会存在相同的常量。
            >只要其中一个是变量，结果就在堆中（堆中对象在将实际地址指向对应常量池内存）
            >如果拼接的结果调用intern()方法，返回值就在常量池中
     */
    @Test
    public void test3(){
        String s1 = "javaEE";
        String s2 = "hadoop";
        String s3 = "javaEEhadoop";
        String s4 = "javaEE"+"hadoop";
        String s5 = s1+"hadoop";
        String s6 = "javaEE"+s2;
        String s7 = s1+s2;

        System.out.println(s3 == s4);//true
        System.out.println(s3 == s5);//false
        System.out.println(s3 == s6);//false
        System.out.println(s3 == s7);//false
        System.out.println(s5 == s6);//false
        System.out.println(s5 == s7);//false
        System.out.println(s6 == s7);//false

        String s8 = s5.intern();//返回得到的s8使用的常量值中已经存在的“javaEEhadoop”
        System.out.println(s3 = s8);//true

        final String s9 = "javaEE";//此处s9用final修饰，是一个常量而非变量
        String s10 = s9+"hadoop";
        System.out.println(s3 == s10);//true
    }

    /*
    String的常用方法
    int length():返回字符串的长度
    char charAt(int index):返回某索引处的字符
    boolean isEmpty():判断是否空字符串
    String toLowerCase():使用默认语言环境，将String中所有字符串转换成小写
    String toUpperCase():使用默认语言环境，将String中所有字符串转换成大写
    String trim():返回字符串的副本，忽略前导空白和尾部空白
    boolean equals(Object anObject)：比较字符串的内容是否相同
    boolean equalsIgnoreCase(String anotherString)：与equals方法类似，忽略大小写，比较字符串的内容是否相同
    String concat(String str):将指定字符串连接到此字符串的结尾。等价于用"+"
    int compareTo(String anotherString):比较两个字符串的大小
    String substring(int beginIndex)：返回一个新的字符串，它是此字符串从beginIndex开始截取（beginIndex包含）
    String substring(int beginIndex, int endIndex)：返回一个新的字符串它是此字符串从beginIndex开始截取到endIndex结束（beginIndex包含，endIndex不包含）--左闭右开
    boolean endsWith(String suffix):测试此字符串是否以指定的后缀结束
    boolean startsWith(String prefix)：测试此字符串是否以指定的前缀开始
    boolean startsWith(String prefix, int toffset):测试此字符串是否从指定索引开始的字符串是否相等
    boolean contains(CharSequence s):当且且仅当此字符串包含指定char值序列时，返回true
    int indexOf(String str)：返回指定字符串在此字符串中第一次出现的索引
    int indexOf(String str, int fromIndex):返回指定字符串在此字符串中第一次出现的索引,从指定的索引开始
    int lastIndexOf(String str)：返回指定字符串在此字符串中最右边处的索引
    int lastIndexOf(String str)：返回指定字符串在此字符串中最右边处的索引
    注：indexOf和lastIndexOf方法如果未找到都是返回-1
    替换
    String replace(CharSequence target, CharSequence replacement):使用指定字面量替换目标字面量
    String replaceAll(String regex, String replacement)：使用给定的replacement替换正则表达式匹配的子字符串
    String replaceFirst(String regex, String replacement):使用给定的replacement替换正则表达式匹配第一个子字符串
    匹配
    boolean matches(String regex)：判断是否匹配与给定的正则表达式
    切片
    String[] split(String regex):根据指定的正则表达式的匹配拆分此字符串
    String[] split(String regex, int limit)：根据指定的正则表达式的匹配拆分此字符串，最多不超过limit个
     */
    @Test
    public void StringMethod(){
        //int length():返回字符串的长度
        String s1 = "helloWorld";
        System.out.println(s1.length());//10
        //char charAt(int index):返回某索引处的字符
        System.out.println(s1.charAt(0));//h
        //boolean isEmpty():判断是否空字符串
        System.out.println(s1.isEmpty());//false
        //String toLowerCase():使用默认语言环境，将String中所有字符串转换成小写
        System.out.println(s1.toLowerCase());//helloworld
        //String toUpperCase():使用默认语言环境，将String中所有字符串转换成大写
        System.out.println(s1.toUpperCase());//HELLOWORLD
        //String trim():返回字符串的副本，忽略前导空白和尾部空白
        System.out.println(s1.trim());//helloworld
        //boolean equals(Object anObject)：比较字符串的内容是否相同
        System.out.println(s1.equals("helloworld"));//false
        //boolean equalsIgnoreCase(String anotherString)：与equals方法类似，忽略大小写，比较字符串的内容是否相同
        System.out.println(s1.equalsIgnoreCase("helloworld"));//true
        //String concat(String str):将指定字符串连接到此字符串的结尾。等价于用"+"
        System.out.println(s1.concat("11"));//helloWorld11
        //int compareTo(String anotherString):比较两个字符串的大小
        System.out.println(s1.compareTo("zju"));//-18
        //String substring(int beginIndex)：返回一个新的字符串，它是此字符串从beginIndex开始截取（beginIndex包含）
        System.out.println(s1.substring(5));//World
        //String substring(int beginIndex, int endIndex)：返回一个新的字符串它是此字符串从
        // beginIndex开始截取到endIndex结束（beginIndex包含，endIndex不包含）--左闭右开
        System.out.println(s1.substring(0,5));//hello
        //boolean endsWith(String suffix):测试此字符串是否以指定的后缀结束
        System.out.println(s1.endsWith("ld"));//true
        //boolean startsWith(String prefix)：测试此字符串是否以指定的前缀开始
        System.out.println(s1.startsWith("he"));//true
        //boolean startsWith(String prefix, int toffset):测试此字符串是否从指定索引开始的字符串是否相等
        System.out.println(s1.startsWith("ll",2));//true
        //boolean contains(CharSequence s):当且且仅当此字符串包含指定char值序列时，返回true
        System.out.println(s1.contains("oW"));//true

        //int indexOf(String str)：返回指定字符串在此字符串中第一次出现的索引
        System.out.println(s1.indexOf("oW"));//4
        //int indexOf(String str, int fromIndex):返回指定字符串在此字符串中第一次出现的索引,从指定的索引开始
        System.out.println(s1.indexOf("o",5));//6
        //int lastIndexOf(String str)：返回指定字符串在此字符串中最右边处的索引
        System.out.println(s1.lastIndexOf("o"));//6
        //int lastIndexOf(String str, int fromIndex)：返回指定字符串在此字符串中最右边处的索引，从指定索引开始
        System.out.println(s1.lastIndexOf("o",5));//4
        //注：indexOf和lastIndexOf方法如果未找到都是返回-1

        //替换
        System.out.println(s1.replace('被','动'));
        System.out.println(s1.replace("被","动"));
        //String replace(CharSequence target, CharSequence replacement):使用指定字面量替换目标字面量
        System.out.println(s1.replace("or","orr"));//helloWorrld
        //String replaceAll(String regex, String replacement)：使用给定的replacement替换正则表达式匹配的子字符串
        System.out.println(s1.replaceAll("\\w{10}","helloworld"));//helloworld
        //String replaceFirst(String regex, String replacement):使用给定的replacement替换正则表达式匹配第一个子字符串
        System.out.println(s1.replaceFirst("\\w","z"));//zelloWorld

        //匹配
        //boolean matches(String regex)：判断是否匹配与给定的正则表达式
        System.out.println(s1.matches("\\w{10}"));//true

        //切片
        //String[] split(String regex):根据指定的正则表达式的匹配拆分此字符串
        System.out.println(Arrays.asList(s1.split("o")));//[hell, W, rld]
        //String[] split(String regex, int limit)：根据指定的正则表达式的匹配拆分此字符串，最多不超过limit个
        System.out.println(Arrays.asList(s1.split("o",2)));//[hell, World]
    }
    /*
    涉及到String类与其他结构之前的转换

    String 与 基本数据类型、包装类之间的转换
        String --> 基本数据类型、包装类：调用包装类的静态方法：parseXxx(str)
        基本数据类型、包装类 --> String:调用String重载的valueOf(xxx)

    String 与 char[]之间的转换
        String --> char[]:调用String的toCharArray()
        char[] --> String:调用String的构造器

   String 与 byte[]之间的转换
        编码：String --> byte[]:调用String的getBytes()
        解码：byte[] --> String：调用String的构造器
   编码：字符串 --> 字节
   解码：字节 --> 字符串 （编码的逆过程）
   说明：解码时，要求解码使用的字符集必须与编码时使用的字符集一致，否则出现乱码
     */
    @Test
    public void StringConvert() throws UnsupportedEncodingException {
        //String --> 基本数据类型、包装类：调用包装类的静态方法：parseXxx(str)
        String str = new String("123");
        int i = Integer.parseInt(str);
        System.out.println(i);//123
        //基本数据类型、包装类 --> String:调用String重载的valueOf(xxx)
        String str2 = String.valueOf(i);
        String str3 = i+"";
        System.out.println(str2+str2.getClass());//"123"

        //String --> char[]:调用String的toCharArray()
        String str1 = "abc123";
        char[] charArray = str1.toCharArray();
        for(int j = 0;j<charArray.length;j++){
            char c=charArray[j];
            System.out.print(c);
            System.out.print(" "+(int)c);
            System.out.println("");
        }
        //char[] --> String:调用String的构造器
        System.out.println(new String(charArray));
        "123".length();
        //String --> byte[]:调用String的getBytes()
        String str4 = "abc123中国";
        //byte[] getBytes()
        byte[] bytes = str4.getBytes();
        System.out.println(Arrays.toString(bytes));//[97, 98, 99, 49, 50, 51, -28, -72, -83, -27, -101, -67]
        //byte[] getBytes(String charsetName)
        byte[] gbks = str4.getBytes("GBK");
        System.out.println(Arrays.toString(gbks));//[97, 98, 99, 49, 50, 51, -42, -48, -71, -6]
        System.out.println(new String(bytes));//使用默认的字符集
        System.out.println(new String(gbks,"gbk"));
    }
    /*
    StringBuilder类

    String、StringBuffer、StringBuilder三者的异同
        String：不可变的字符序列。底层使用char[]进行存储。
        StringBuffer：可变的字符序列，线程安全，效率低,底层用char[]存储
        StringBuild：可变的字符序列，线程不安全，效率高，jdk5.0新增的,底层用char[]存储

    源码分析：
        String str = new String();//char[] value = new char[0]
        String str1 = new String("abc");//char[] value = new char['a','b','c']

        StringBuffer sb1 = new StringBuffer();//char[] value = new char[16];底层创建了一个长度为16的数组
        sb1.append('a');//value[0] = 'a';
        sb1.append('b');//value[1] = 'b';

        StringBuffer sb1 = new StringBuffer("abc");//char[] value = new char["abc".length+16];
        底层创建了一个长度为"abc".length+16的数组

        开发中建议使用：StringBuffer(int capacity) 或 StringBuilder(int capacity)

     StringBuffer的常用方法：
        StringBuffer append(xxx)：提供了很多append方法，用于进行字符串的拼接
        StringBuffer delete(int start, int end):删除[start,end)位置的内容
        StringBuffer replace(int start, int end, String str):把[start,end)位置替换为str
        StringBuffer insert(int offset, xxx):在指定位置插入xxx
        StringBuffer reverse():把当前字符序列逆转
        int indexOf(String str):返回str第一次出现的索引位置
     */
    @Test
    public void StringBuilderStringBufferTest(){
        StringBuffer stringBuffer = new StringBuffer();
        //StringBuffer append(xxx)：提供了很多append方法，用于进行字符串的拼接
        stringBuffer.append("abcdefg");
        //StringBuffer delete(int start, int end):删除[start,end)位置的内容
        stringBuffer.delete(1,2);
        System.out.println(stringBuffer.toString());//acdefg
        //StringBuffer replace(int start, int end, String str):把[start,end)位置替换为str
        stringBuffer.replace(3,4,"zz");//acdzzfg
        System.out.println(stringBuffer.toString());
        //StringBuffer insert(int offset, xxx):在指定位置插入xxx
        stringBuffer.insert(0,"q");
        System.out.println(stringBuffer.toString());//qacdzzfg
        //StringBuffer reverse():把当前字符序列逆转
        stringBuffer.reverse();
        System.out.println(stringBuffer.toString());//gfzzdcaq
        //int indexOf(String str):返回str第一次出现的索引位置
        System.out.println(stringBuffer.indexOf("zz"));//2
        //CharSequence subSequence(int start, int end):返回一个[start,end)索引区间的子字符串
        System.out.println(stringBuffer.subSequence(1,3));//fz
        //int length():
        System.out.println(stringBuffer.length());//8
        System.out.println(stringBuffer.charAt(3));//z
        stringBuffer.setCharAt(3,(char)64);//gfzdcaq
        System.out.println(stringBuffer);
    }
}
