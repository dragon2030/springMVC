package com.bigDragon.lombok.printCase;

import com.bigDragon.lombok.printCase.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 * 疑问解决lombok的@Data注解无法打印继承的父类信息问题
 * 参考播客 https://www.jb51.net/program/330116r71.htm
 */
public class Main {
        private static Map<String, Thread> taskThreadMap = new ConcurrentHashMap<>();
        
        public static void main(String[] args) {
            People people = new People();
            people.setName("John");
            people.setAge(29);
            people.setHobby("play computer game");
            System.out.print("POJO: ");
            System.out.println(people);


            People2 people2 = new People2();
            people2.setName("John");
            people2.setAge(29);
            people2.setHobby("play computer game");
            System.out.print("@Data: ");
            System.out.println(people2);

            People3 people3 = new People3();
            people3.setName("John");
            people3.setAge(29);
            people3.setHobby("play computer game");
            System.out.print("本身重写toString，JSON.toJSONString(: ");
            System.out.println(people3);

            People4 people4 = new People4();
            people4.setName("John");
            people4.setAge(29);
            people4.setHobby("play computer game");
            people4.setfName("fName");
            System.out.print("@Data继承的父类没有@Data:  ");
            System.out.println(people4);

            People5 people5 = new People5();
            people5.setName("John");
            people5.setAge(29);
            people5.setHobby("play computer game");
            people5.setfName("fName");
            System.out.print("@Data继承父类，本身重写toString，JSON.toJSONString(:  ");
            System.out.println(people5);

            People6 people6 = new People6();
            people6.setName("John");
            people6.setAge(29);
            people6.setHobby("play computer game");
            people6.setfName("fName");
            System.out.print("@Data继承的父类有@Data:  ");
            System.out.println(people6);

            People7 people7 = new People7();
            people7.setName("John");
            people7.setAge(29);
            people7.setHobby("play computer game");
            people7.setfName("fName");
            System.out.print("@ToString(callSuper = true):  ");
            System.out.println(people7);

            People8 people8 = new People8();
            people8.setName("John");
            people8.setAge(29);
            people8.setHobby("play computer game");
            people8.setfName("fName");
            System.out.print("本身是pojo父类是pojo:  ");
            System.out.println(people8);
        }

    @RequestMapping("/testCase")
    @ResponseBody
    public People8 m1(){
        People8 people8 = new People8();
        people8.setName("John");
        people8.setAge(29);
        people8.setHobby("play computer game");
        people8.setfName("fName");
        System.out.print("本身是pojo父类是pojo:  ");
        System.out.println(people8);
        return people8;
    }

}
