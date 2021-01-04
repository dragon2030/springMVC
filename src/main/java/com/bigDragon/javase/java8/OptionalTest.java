package com.bigDragon.javase.java8;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Optional;

/**
 * Optional类:为了在程序中避免出现空指针异常而创建的
 * 一、概念
 *      >到目前为止，臭名昭著的空指针异常是导致Java应用程序失败的最常见原因。以前，为了解决空指针
 *      异常，Google公司著名的Guava项目引入了Optional类，Guava通过使用检测空值的方式来防止代码
 *      污染，它鼓励程序员写更干净的代码。受Google Guava的启发，Optional类已经成为Java8类库中的
 *      一部分
 *      >Optional<T>类（java.util.Optional）是一个容器类，它可以保存类型T的值，代表这个值存在。
 *      或者仅仅保存null，表示这个值不存在。原来用null表示一个值不存在，现在Optional可以更好的表达
 *      这个概念。并且可以避免空指针异常。
 *      >Optional类的Javadoc描述如下：这是一个可以为null的容器对象。如果值存在则isPresent()方法
 *      会返回true，调用get()方法会返回该对象。
 * 二、创建Option类对象的方法
 *      static <T> Optional<T> of(T value):创建一个Optional实例，t必须非空
 *      static<T> Optional<T> empty():创建一个空的 Optional实例
 *      static <T> Optional<T> ofNullable(T t):创建一个Optional实例，t可以为null
 * 三、判断Optional容器是否包含对象
 *      boolean isPresent()——判断是否包含对象
 * 四、获取Optional容器的对象
 *      T get()——如果调用对象包含值，返回该值，否则抛出异常（与Optional.of()对应，明确知道非空时使用）
 *      T orElse(T other)——如果有值则将其返回，否则返回指定的other对象(与Optional.ofNullable()对应，不清楚是否为空时使用)
 *
 * @author bigDragon
 * @create 2020-12-21 19:31
 */
public class OptionalTest {
    public static void main(String[] args) {
        OptionalTest optionalTest = new OptionalTest();
        //创建Optional类对象的方法
        optionalTest.test1();
        //判断Optional容器是否包含对象
        optionalTest.test2();
        //获取Optional容器的对象
        optionalTest.test3();
    }
    /*
    创建Optional类对象的方法
     */
    @Test
    public void test1(){
        Girl girl = new Girl();
        Girl girl2 = null;
        //static <T> Optional<T> of(T value):创建一个Optional实例，t必须非空
        Optional<Girl> optionalGirl = Optional.of(girl);
        System.out.println(optionalGirl);
        //static<T> Optional<T> empty():创建一个空的 Optional实例
        Optional<Object> empty = Optional.empty();
        System.out.println(empty);
        //static <T> Optional<T> ofNullable(T t):创建一个Optional实例，t可以为null
        Optional<Girl> optionalGirl1 = Optional.ofNullable(girl);
        System.out.println(optionalGirl1);
        Optional<Girl> optionalGirl2 = Optional.ofNullable(girl2);
        System.out.println(optionalGirl2);
    }
    /*
    判断Optional容器是否包含对象
     */
    @Test
    public void test2(){
        //boolean isPresent()——判断是否包含对象
        Girl girl = new Girl();
        Optional<Girl> optionalGirl = Optional.of(girl);
        boolean present = optionalGirl.isPresent();
        System.out.println(present);
        Girl girl2 = null;
        Optional<Girl> girl21 = Optional.ofNullable(girl2);
        System.out.println(girl21.isPresent());
    }
    /*
    获取Optional容器的对象
     */
    @Test
    public void test3(){
        Girl girl = new Girl();
        //T get()——如果调用对象包含值，返回该值，否则抛出异常（与Optional.of()对应，明确知道非空时使用）
        Optional<Girl> optionalGirl = Optional.of(girl);
        Girl girl1 = optionalGirl.get();
        System.out.println(girl1);
        System.out.println("****************************");
        //T orElse(T other)——如果有值则将其返回，否则返回指定的other对象(与Optional.ofNullable()对应，不清楚是否为空时使用)
        girl = null;
        Optional<Girl> optionalGirl2 = Optional.ofNullable(girl);
        Girl girl2 = optionalGirl2.orElse(new Girl("波波"));
        System.out.println(girl2);
    }
}
