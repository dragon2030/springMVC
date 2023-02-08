package com.bigDragon.lombok;

import com.alibaba.fastjson.JSON;
import com.bigDragon.jsonParse.jackson.JsonFormatTest;
import org.junit.Test;

/**
 * @author: bigDragon
 * @create: 2022/12/20
 * @Description:
 */
public class LombokTest {
    @Test
    public void test1(){
        Animal animal = new Animal();
        animal.setName("Bob");
        System.out.println(animal);
    }
    @Test
    public void test2(){
        Animal2 animal2 = new Animal2().setName("Bob").setAge(8).setMasterName("John").setId("123");
        System.out.println(animal2);
    }
}
