package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;


import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Java 浅拷贝和深拷贝
 *
 * 应用场景：开发过程中，有时会遇到把现有的一个对象的所有成员属性拷贝给另一个对象的需求。
 *
 * 应用背景：
 *      Java中的数据类型分为基本数据类型和引用数据类型。对于这两种数据类型，在进行赋值操作、用作方法参数或返回值时，会有值传递和引用地址传递的差别
 *
 *      如果在拷贝这个对象的时候，只对基本数据类型进行了拷贝，而对引用数据类型只是进行引用的传递，而没有真实的创建一个新的对象，则认为是浅拷贝。
 *      反之，在对引用数据类型进行拷贝的时候，创建了一个新的对象，并且复制其内的成员变量，则认为是深拷贝。
 *
 * @author bigDragon
 * @create 2020-09-19 9:46
 */
public class ShallowCopyDeepCopy {
    public static void main(String[] args){
        ShallowCopyDeepCopy shallowCopyDeepCopy = new ShallowCopyDeepCopy();
        //对象拷贝
        //传递对象的引用，实际上是同一个值
        System.out.println("对象拷贝");
        shallowCopyDeepCopy.ObjectCloneTest();
        //浅拷贝
        //对基本数据类型进行值传递，对引用数据类型进行引用传递般的拷贝，此为浅拷贝
        System.out.println("浅拷贝");
        shallowCopyDeepCopy.ShallowCopy();
        //深拷贝
        //对基本数据类型进行值传递，对引用数据类型，创建一个新的对象，并复制其内容，此为深拷贝。
        System.out.println("深拷贝");
        shallowCopyDeepCopy.DeepCopy();
    }

    /**
     * 对象拷贝
     */
    @Test
    public void ObjectCloneTest(){
        //拷贝user1到user2中
        ClonePOJO clonePOJO = new ClonePOJO();
        clonePOJO.setStr1("str1");
        clonePOJO.setInt1(1);
        User user1 = new User();
        user1.setName("Mike");
        user1.setAge("22");
        clonePOJO.setUser(user1);
        ClonePOJO cloneTest2 = clonePOJO;
        //改变user2属性
        cloneTest2.setStr1("已久修改对象");
        cloneTest2.setInt1(0);
        User user2=cloneTest2.getUser();
        user2.setName("name已久修改对象");
        user2.setAge("age已久修改对象");
        cloneTest2.setUser(user2);
        System.out.println("目标对象:"+ clonePOJO);
        System.out.println("拷贝结果对象:"+cloneTest2);
    }

    /**
     * 浅拷贝
     */
    @Test
    public void  ShallowCopy(){
        //拷贝user1到user2中
        ClonePOJO2 clonePOJO2 = new ClonePOJO2();
        clonePOJO2.setStr1("str1");
        clonePOJO2.setInt1(0);
        clonePOJO2.setDate1(new Date(123,01,01));
        clonePOJO2.setDecimal1(BigDecimal.ZERO);
        //user属性
        User user1 = new User();
        user1.setName("Mike");
        user1.setAge("22");
        clonePOJO2.setUser(user1);
        ClonePOJO2 cloneTest2 = (ClonePOJO2)clonePOJO2.clone();

        System.out.println("修改前-目标对象:"+ clonePOJO2);
        System.out.println("修改前-拷贝结果对象:"+cloneTest2);
        //改变user2属性
        cloneTest2.setStr1("已修改对象");
        cloneTest2.setInt1(1);
        Date date_change = clonePOJO2.getDate1();
        date_change.setTime(0l);
        clonePOJO2.setDate1(date_change);
        User user2=cloneTest2.getUser();
        user2.setName("name已修改对象");
        user2.setAge("age已修改对象");
        cloneTest2.setUser(user2);
        System.out.println("修改后-目标对象:"+ clonePOJO2);
        System.out.println("修改后-拷贝结果对象:"+cloneTest2);
    }

    /**
     * 深拷贝
     */
    public void DeepCopy(){
        //拷贝user1到user2中
        ClonePojo3 clonePojo3 = new ClonePojo3();
        clonePojo3.setStr1("str1");
        clonePojo3.setInt1(1);
        User2 user2 = new User2();
        user2.setName("Mike");
        user2.setAge("22");
        clonePojo3.setUser2(user2);
        ClonePojo3 cloneTest3 = (ClonePojo3)clonePojo3.clone();
        //改变user2属性
        cloneTest3.setStr1("已修改对象");
        cloneTest3.setInt1(0);
        User2 user3=cloneTest3.getUser2();
        user3.setName("name已修改对象");
        user3.setAge("age已修改对象");
        cloneTest3.setUser2(user3);
        System.out.println("目标对象:"+ clonePojo3);
        System.out.println("拷贝结果对象:"+cloneTest3);
    }


    //项目中的深拷贝
//    public FeeReciveQuery clone() throws CloneNotSupportedException {
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(this);
//
//            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
//            ObjectInputStream ois = new ObjectInputStream(bis);
//            return (FeeReciveQuery) ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            throw new JeecgBootException("操作失败,请联系管理员");
//        }
//    }
}
