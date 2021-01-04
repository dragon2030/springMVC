package com.bigDragon.javase.collection.map;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Properties
 *
 * 1.Properties类是Hashtable的子类，该对象用于处理属性文件
 * 2.由于属性文件里的key、value都是字符串类型，所以Properties里的key和value都是字符串类型
 * 3.存取数据时，建议使用setProperty(String key,String value)方法和getProperty(String key)方法
 *
 *
 * @author bigDragon
 * @create 2020-11-20 15:52
 */
public class PropertiesTest {
    @Test
    public void test(){
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream=new FileInputStream("src\\main\\resources\\db.properties");
            properties.load(fileInputStream);
            String url = properties.getProperty("jdbc.url");
            System.out.println(url);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
