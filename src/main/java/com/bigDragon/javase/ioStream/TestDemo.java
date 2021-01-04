package com.bigDragon.javase.ioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * io测试练习类
 *
 * @author bigDragon
 * @create 2020-12-31 11:32
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {
        TestDemo testDemo = new TestDemo();
        //在指定路径中查看有多少条手机号码，文件格式如13757149598,18758294303,13967627711,。。。
        testDemo.test1();
    }
    /*
    在指定路径中查看有多少条手机号码，文件格式如13757149598,18758294303,13967627711,。。。
     */
    public void test1() throws Exception{
        File file = new File("D:\\ioReadTest\\记录3.txt");
        //转换流
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
        char[] cbuffer = new char[1024];
        int len = 0;
        StringBuffer stringBuffer = new StringBuffer();
        while ((len = inputStreamReader.read(cbuffer))!=-1){
            String s = new String(cbuffer, 0, len);
            stringBuffer.append(s);
        }
        inputStreamReader.close();
        System.out.println(stringBuffer.toString().split(",").length);

    }
}
