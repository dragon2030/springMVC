package com.bigDragon.javase.reflect;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 直接实例化和反射的对比
 *
 * 疑问1.通过直接new的方式或反射的方式都可以调用公共的结构，开发中到底用哪个？
 *      建议：直接new的方式
 *          反射的特性：动态性
 * 疑问2：反射机制和面向对象中的封装性是不是矛盾？如何看待两个技术
 *      不矛盾。封装性解决建议调用使用的问题，反射解决能不能调用的问题。
 *
 * 关于java.lang.Class类的理解
 * 1.类的加载过程：（后有类加载过程详解）
 * 程序经过javac.exe命令以后，会生成一个或多个字节码文件 (.class结尾)
 * 接着我们使用java.exe命令对某个字节码文件进行解释运行。相当于将某个字节码文件加载到内存中。此过程就称为类的加载。
 * 加载到内存中的类，我们就称为运行时类，此运行时类，就作为Class的一个实例。
 *
 * 2.换句话说，Class的实例就对应着一个运行时类。
 * 3.加载到内存中的运行时类，会缓存一定的时间。在此时间之内，我们可以通过不同的方式来获取此运行时类。
 *
 * 哪些类型可以有Class对象
 * 1.class：外部类，成员（成员内部类、静态内部类），局部内部类。匿名内部类
 * 2.interface:接口
 * 3.[]数组
 * 4.enum:枚举
 * 5.annotation: 注解@interface
 * 6.primitive type:基本数据类型
 * 7.void
 *
 * 类的加载过程：
 *      当主程序主动使用某个类时，如果该类还未被加载到内存中，则系统会通过如下三个步骤来对该类进行初始化。
 *          类的加载（Load）:将类的class文件读入内存，并为值创建一个java.lang.Class对象，此过程由类加载器完成。
 *              加载：将class文件字节码内容加载到内存中，并将这些静态数据转换成方法区的运行时数据结构，然后在堆中
 *              生成一个代表这个类的java.lang.Class对象，作为方法区中类数据的访问入口(即引用地址)。所有需要访问
 *              和使用的类数据只能通过这个Class对象，这个加载过程需要类加载器参与。
 *          类的链接(Link):将类的二进制数据合并到JRE中。
 *              链接：将Java类中的二进制代码合并到JVM的运行状态之中的过程。
 *                  >验证：确保加载的类信息符合JVM规范，例如：以cafe开头，没有安全方面的问题
 *                  >准备：正式为类变量(static)分配内存并设置类默认初始值的阶段，这个内存都将方法去中进行分配
 *                  >解析: 虚拟机常量池内符号引用(常量名)替换为直接引用(地址)的过程。
 *          类的初始化(Initialize):JVM负责对类进行初始化。
 *              >执行类构造器<clinit>()方法的过程。类构造器<clinit>()方法是由编译期自动收集类中所有类变量的赋值
 *              动作和静态代码块中的语句合并产生的。(类构造器是构造类信息的，不是构造该类对象的构造器)
 *              >当初始化一个类的时候，如果发现其父类还没有进行初始化，则需要先触发其父类的初始化。
 *              >虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确的加锁和同步
 *
 *  初始化后，m的值由<clinit>()方法执行决定
 *        这个ClassLoaderTest的类构造器<clinit>()由类变量的赋值和静态代码库中的语句安装顺序合并产生，类似于
 *        <clinit>(){
 *            m = 300;
 *            m = 100;
 *        }
 *        设计知识：静态代码块和显示赋值
 *
 *  类加载流程：源程序(*.java)-->Java编译器-->字节码(*.class文件)-->类装载器(类加载器)-->字节码校验器
 *  -->解释器-->操作系统平台
 *
 *  类缓存：标准的JavaSE类加载器可以按要求查找类，但一旦某个类被加载到加载器中，它将维持加载(缓存)一段时间。
 *  不过JVM垃圾回收机制可以回收这些Class对象。
 *
 *
 * @author bigDragon
 * @create 2020-12-01 16:04
 */
public class ReflectionTest {

    public static void main(String[] args) throws ClassNotFoundException {
            ReflectionTest reflectionTest = new ReflectionTest();
            //反射之前，对于Person的操作
            reflectionTest.test1();
            //反射之后，对于Person的操作
            reflectionTest.test2();
            //获取Class的实例方式
            reflectionTest.test4();
            //Class实例可以是哪些结构的说明
            reflectionTest.test5();
            //类构造器初始化赋值顺序
            new ClinitTest();
    }
    /**
     * 反射之前，对于Person的操作
     */
    @Test
    public void test1(){
        //1.创建Person类的对象
        Person person=new Person("Tom",12);

        //2.通过对象，调用其内部的属性、方法
        person.age = 10;
        System.out.println(person);
        person.show();
        //在Person类外部，不可以通过Person类的对象调用其内部私有结构。
        //比如：name、showNation()以及私有的构造器
    }

    /**
     * 反射之后，对于Person的操作
     */
    @Test
    public void test2(){
        try {
            Class clazz = Person.class;

            //1.通过反射，创建Person类的对象
            Constructor constructor = clazz.getConstructor(String.class, int.class);
            Object o = constructor.newInstance("Tom", 12);
            Person p = (Person) o;
            System.out.println(p);
            //无参构造器
            Person p2 = (Person)clazz.newInstance();
            System.out.println(p2);

            //2.通过反射，调用对象指定的属性、方法
            //调用属性
            Field age = clazz.getDeclaredField("age");
            age.set(p,10);
            System.out.println(p);

            //调用方法
            Method show = clazz.getDeclaredMethod("show");
            show.invoke(p);

            System.out.println("***************************");

            //通过反射，可以调用Person类的私有结构的。比如：私有的构造器、方法、属性。
            //调用私有的构造器
            Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
            Object o1 = declaredConstructor.newInstance("Jerry");
            Person p1 = (Person) o1;
            System.out.println(p1);

            //调用私有的属性
            Field name = clazz.getDeclaredField("name");
            name.setAccessible(true);
            name.set(p1,"HanMeimei");
            System.out.println(p1);
            
            //调用私有的方法
            Method showNation = clazz.getDeclaredMethod("showNation", String.class);
            showNation.setAccessible(true);
            String nation = (String) showNation.invoke(p1,"中国");//相当于p1.showNation(中国)
            System.out.println(nation);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自测
     */
    @Test
    public void test3(){
        try {
            Class clazz = Person.class;
            Person person=new Person("Tom",12);
            Field name = clazz.getDeclaredField("name");
            name.setAccessible(true);
            name.set(person,"HanMeimei");
            System.out.println(person);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Class的实例方式（前三种方式需要掌握）
     */
    @Test
    public void test4() throws ClassNotFoundException {
        //方式一：调用运行时类的属性：.class
        Class clazz1 = Person.class;
        System.out.println(clazz1);
        //方式二：通过运行时类的对象,调用getClass()
        Person person = new Person();
        Class clazz2 = person.getClass();
        System.out.println(clazz2);
        //方式三：调用Class的静态方法：forName(String classPath)
        //使用频率更高，更好的提现了动态性
        Class clazz3 = Class.forName("com.bigDragon.javase.reflect.Person");
        System.out.println(clazz3);

        System.out.println(clazz1 == clazz2);
        System.out.println(clazz1 == clazz3);

        //方式四：使用类的加载器：ClassLoader
        ClassLoader classLoader = ReflectionTest.class.getClassLoader();
        Class<?> clazz4 = classLoader.loadClass("com.bigDragon.javase.reflect.Person");
        System.out.println(clazz4);
        System.out.println(clazz1 == clazz4);
    }

    /**
     * Class实例可以是哪些结构的说明：
     *
     * 哪些类型可以有Class对象
     * 1.class：外部类，成员（成员内部类、静态内部类），局部内部类。匿名内部类
     * 2.interface:接口
     * 3.[]数组
     * 4.enum:枚举
     * 5.annotation: 注解@interface
     * 6.primitive type:基本数据类型
     * 7.void
     */
    @Test
    public void test5(){
        Class<Object> objectClass = Object.class;
        Class<Comparable> comparableClass = Comparable.class;
        Class<String[]> aClass = String[].class;
        Class<int[][]> aClass1 = int[][].class;
        Class<ElementType> elementTypeClass = ElementType.class;
        Class<Override> overrideClass = Override.class;
        Class<Integer> integerClass = int.class;
        Class<Void> voidClass = void.class;
        Class<Class> classClass = Class.class;

        int[] ints = new int[10];
        int[] ints1 = new int[100];
        Class<? extends int[]> aClass2 = ints.getClass();
        Class<? extends int[]> aClass3 = ints1.getClass();
        //只要元素类型与维度一样，就是同一个Class
        System.out.println(aClass2 == aClass3);
    }
}
