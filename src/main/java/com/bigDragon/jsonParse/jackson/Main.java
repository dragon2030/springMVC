package com.bigDragon.jsonParse.jackson;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        //jackson对json格式转换
        new com.bigDragon.jsonParse.jackson.JacksonTest();
        //@JsonFormat注解的使用
        new com.bigDragon.jsonParse.jackson.JsonFormatTest();
    }
}
