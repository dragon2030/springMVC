package com.bigDragon.javase.baseTest;

import org.junit.Test;

/**
 * @author bigDragon
 * @create 2021-03-30 19:13
 */
public class FunctionTest {
    //递归调用
    @Test
    public void test1(){
        int sum = sum(100);
        System.out.println("合计："+sum);//5050
    }
    //从1开始累加到i的数字之和
    public int sum(int i){
        if(i==1){
            return 1;
        }else {
            return i+sum(i-1);
        }
    }
    @Test
    public void test2(){
        int sum = fibonacci(10);
        System.out.println("数值为："+sum);//55
    }
    /**
     * 斐波那契数列
     * @param num 第num步
     * @return
     */
    public int fibonacci(int num){
        if(num == 1 || num == 2){
            return 1;
        }else {
            return fibonacci(num-1)+fibonacci(num-2);
        }
    }

}
