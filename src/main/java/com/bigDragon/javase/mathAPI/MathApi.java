package com.bigDragon.javase.mathAPI;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author bigDragon
 * @create 2021-03-05 13:46
 */
public class MathApi {
    public static void main(String[] args) {
        int abs = Math.abs(-1);
        System.out.println(abs);//1
        double random = Math.random();
        System.out.println(random);//0.12441732805323702
        long round = Math.round(4.5);
        System.out.println(round);//5
    }
    @Test
    public void test1(){
        Random random = new Random();
        for(int i=0;i<10;i++){
            int numbers = random.nextInt(10)+1;
            System.out.println(numbers);
        }
    }
    @Test
    public void test2(){
        //BigInteger
        BigInteger bigInteger = new BigInteger("12345675434567434567897654345678654333");
        System.out.println(bigInteger);//12345675434567434567897654345678654333

        BigDecimal bigDecimal = new BigDecimal("3432.5");
        BigDecimal bigDecimal1 = new BigDecimal("32.21");
        //四舍五入（默认）
        BigDecimal divide1 = bigDecimal.divide(bigDecimal1, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide1);//106.6
        //保留15位小数
        BigDecimal divide2 = bigDecimal.divide(bigDecimal1, 15, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide2);//106.566283762806579
    }

    /**
     * Math的floor和ceil
     * BigDecimal.setScale用法总结
     */
    @Test
    public void case_20220329(){
/*        double d = 3.1415926;
        System.out.println(Math.floor(d));
        System.out.println(Math.ceil(d));*/

        //参考博客https://blog.csdn.net/qq_39101581/article/details/78624617
        //6 和 7 没测
        BigDecimal bigDecimal = new BigDecimal("3.1415926");
        BigDecimal bigDecimal0 = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
        System.out.println(bigDecimal0);//跟ROUND_DOWN相反，进位处理
        BigDecimal bigDecimal1 = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        System.out.println(bigDecimal1);//直接去掉多余的位数
        BigDecimal bigDecimal2 = bigDecimal.setScale(2, BigDecimal.ROUND_CEILING);
        System.out.println(bigDecimal2);//天花板（向上），正数进位向上，负数舍位向上
        BigDecimal bigDecimal3 = bigDecimal.setScale(2, BigDecimal.ROUND_FLOOR);
        System.out.println(bigDecimal3);//地板（向下），正数舍位向下，负数进位向下
        BigDecimal bigDecimal4 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(bigDecimal4);//四舍五入（若舍弃部分>=.5，就进位）
        BigDecimal bigDecimal5 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        System.out.println(bigDecimal5);//四舍五入（若舍弃部分>.5,就进位）
/*        BigDecimal bigDecimal6 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        System.out.println(bigDecimal6);
        BigDecimal bigDecimal7 = bigDecimal.setScale(2, BigDecimal.ROUND_UNNECESSARY);
        System.out.println(bigDecimal7);*/
    }
}