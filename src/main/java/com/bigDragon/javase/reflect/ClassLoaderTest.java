package com.bigDragon.javase.reflect;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 类的加载和构造器ClassLoader的理解
 *
 * 了解ClassLoader:
 *      类加载器作用是把类（class）装载进内存的。JVM规范定义了如下类型的类的加载器。
 *          Bootstrap Classloader:引导类加载器（启动类加载器）——用C++编写的，是JVM自带的类加载器，复制java平台核心库，用来加载核心类库。该类无法直接获取。
 *          Extension Classloader:扩展类加载器——负责< JAVA_HOME >\lib\ext目录下的jar包或-D java.ext.dirs指定目录下的jar包装入工作库
 *          System Classloader:系统类加载器（应用程序类加载器）——负责java-classpath或-D java.class.path所指的目录下的类与jar包装入工作，是最常用的加载器。
 *              它负责加载用户类路径（ClassPath）上所指定的类库，开发者可以直接使用这个类加载器，如果应用程序中没有自定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。
 *          自定义类加载器：
 *  
 *  问：双亲委派
 *  https://blog.csdn.net/u010020088/article/details/144293349?ops_request_misc=&request_id=&biz_id=102&utm_term=%E5%8F%8C%E4%BA%B2%E5%A7%94%E6%B4%BE%E6%9C%BA%E5%88%B6&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-0-144293349.nonecase&spm=1018.2226.3001.4187
 *  阿里二面：双亲委派机制？原理？能打破吗？
 *
 * @author bigDragon
 * @create 2020-12-03 11:23
 */
public class ClassLoaderTest {
    public static void main(String[] args){
        ClassLoaderTest classLoaderTest = new ClassLoaderTest();
        //类的加载器分类
        classLoaderTest.test1();
        //Properties用来读取配置文件文件
        classLoaderTest.test2();
    }

    /**
     * 类的加载器分类
     */
    @Test
    public void test1(){
        //对于自定义类，使用系统类加载器进行加载
        ClassLoader classLoader = this.getClass().getClassLoader();
        System.out.println(classLoader);//sun.misc.Launcher$AppClassLoader@18b4aac2--类加载器
        //调用系统类加载器的getParent():获取扩展类加载器
        ClassLoader parent = classLoader.getParent();
        System.out.println(parent);//sun.misc.Launcher$ExtClassLoader@4769b07b--扩展类加载器
        //调用扩展类加载器的getParent():无法获取引导类加载器
        //引导类加载器主要负责加载java核心类库，无法加载自定义类。
        ClassLoader parent1 = parent.getParent();
        System.out.println(parent1);//null--无法获取引导类加载器

        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1);//null 证明核心类的类加载器有，但无法被获取
    }

    /**
     * Properties用来读取配置文件文件
     */
    @Test
    public void test2(){
        //方法一：

/*        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream=new FileInputStream("src\\Main\\resources\\db.properties");
            properties.load(fileInputStream);
            String url = properties.getProperty("jdbc.url");
            System.out.println(url);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //方法二
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceAsStream = classLoader.getResourceAsStream("db.properties");
            properties.load(resourceAsStream);
            String url = properties.getProperty("jdbc.url");
            System.out.println(url);
            resourceAsStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
