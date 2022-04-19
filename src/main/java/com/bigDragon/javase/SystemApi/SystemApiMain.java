package com.bigDragon.javase.SystemApi;

import java.util.Properties;

/**
 * @author bigDragon
 * @create 2020-12-25 11:19
 */
public class SystemApiMain {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        System.out.println(l);//1614917691898
        Properties properties = System.getProperties();
        System.out.println(properties);//所有系统属性
        String property = System.getProperty("java.version");
        System.out.println(property);//1.8.0_91
        String property2 = System.getProperty("java.home");
        System.out.println(property2);//C:\Program Files\Java\jdk1.8.0_91\jre
        String property3 = System.getProperty("os.name");
        System.out.println(property3);//Windows 10
        String property4 = System.getProperty("os.version");
        System.out.println(property4);//10.0
        String property5 = System.getProperty("user.name");
        System.out.println(property5);//G003759
        String property6 = System.getProperty("user.home");
        System.out.println(property6);//C:\Users\3759
        String property7 = System.getProperty("user.dir");
        System.out.println(property7);//C:\Users\3759\IdeaProjects\springMVC
    }
}
