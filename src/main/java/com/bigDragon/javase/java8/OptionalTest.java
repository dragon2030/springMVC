package com.bigDragon.javase.java8;

import com.bigDragon.model.User;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


        //生产案例ofNullable orElse
        //优雅的规避空指针
        optionalTest.case_20221024();
        //获取流中第一个值
        optionalTest.case_20230207();
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
        Girl girl2 = optionalGirl2.orElse(new Girl("bb"));
        System.out.println(girl2);
    }

    @Test
    public void case_20231205(){
//        .empty()： 创建一个空的Optional实例\
        Optional<Object> empty = Optional.empty();
//        System.out.println(empty.get());//java.util.NoSuchElementException: No value present
//        .of(T t): 创建一个Optional 实例，为null时报异常
        User user = new User();
        Optional<User> user1 = Optional.of(user);
        System.out.println(user1.get());//User1468357786{userId=0, age='null', name='null', peopleDes='null', sexId='null'}
//        .ofNullable(T t): 若t 不为null,创建Optional 实例,否则创建空实例
        User user2 = null;
        User user3 = Optional.ofNullable(user2).orElse(new User());
        System.out.println(user3);
//        isPresent(): 判断容器中是否有值
//        ifPresent(Consume lambda)： 容器若不为空则执行括号中的Lambda表达式
//        orElse(T t): 获取容器中的元素，若容器为空则返回括号中的默认值
//        orElseGet(Supplier s): 如果调用对象包含值，返回该值，否则返回s 获取的值
//        orElseThrow(): 如果为空，就抛出定义的异常，如果不为空返回当前对象
//        map(Function f): 如果有值对其处理，并返回处理后的Optional，否则返回Optional.empty()
//        flatMap(Function mapper): 与map 类似，要求返回值必须是Optional
//        T get()： 获取容器中的元素，若容器为空则抛出NoSuchElement异常
    }

    //生产案例ofNullable orElse
    //优雅的规避空指针
    @Test
    public void case_20221024(){
        Integer meterNum=12;
        Integer virMeterNum=12345;
        Integer s = Optional.ofNullable(meterNum).orElse(virMeterNum);
        System.out.println(s);
    }

    //获取流中第一个值
    @Test
    public void case_20230207(){
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        Optional<String> first = strings.stream().findFirst();
        System.out.println(first.isPresent());
        if(first.isPresent()){
            System.out.println(first.get());
        }
    }
}
