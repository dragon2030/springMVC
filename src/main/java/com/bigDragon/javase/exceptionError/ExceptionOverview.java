package com.bigDragon.javase.exceptionError;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 异常概述
 *
 * 异常：在Java语言中，将程序执行发生的不正确情况称为“异常”。
 *
 * Java程序在执行过程中所发生的异常事件可分为两类：
 *      Error：Java虚拟机无法解决的严重问题。如JVM系统内部错误、资源耗尽等严重情况。比如StackOverflowError和OOM。一般不编写针对性代码进行处理。
 *      Exception:其他因编程错误或偶然的外在因素导致的一般性问题，可以使用针对性的代码进行处理。
 *
 * 对于错误，一般有两种解决方法：
 *      遇到错误就终止程序的运行。
 *      由程序员在编写程序时，就考虑到错误的检测、错误消息的提示，以及错误的处理。
 *
 * 从异常的发生位置可以分为：
 *      编译时异常：非受检（unchecked）异常。例如Error，Runtime Exception等
 *      运行时异常：受检（checked）异常。例如IOException，ClassNotFund Exception等
 *
 * 异常体系结构
 *      java.lang.Throwable
 *          java.lang.Error:一般不编译针对的代码进行处理
 *          java.lang.Exception:可以进行异常的处理
 *              编译时异常（checked）
 *                  IOException
 *                      FileNotFoundException
 *                  ClassNotFoundException
 *              运行时异常（unchecked）
 *                  NullPointException
 *                  ArrayIndexOutOfBoundsException
 *                  ClassCastException
 *                  NumberFormatException
 *                  InputMismatchException
 *
 *  异常处理模型：抓抛模型
 *      过程一：“抛”：程序在正常执行的过程中，一旦出现异常，就会在异常代码处生成一个对应异常类的对象，并将此对象抛出。一旦抛出此对象后，其后的代码将不执行
 *
 *      关于异常对象的产生：
 *          1.系统自动生成的异常对象
 *          2.手动的生成一个异常对象，并抛出（throw）
 *
 *      过程二：“抓”：可以理解为异常的处理方式，有两种处理方式。
 *          try-catch-finally
 *          throws+异常类型
 *
 *
 * @author bigDragon
 * @create 2020-10-21 16:29
 */
public class ExceptionOverview {
    public static void main(String[] args){
        //栈溢出：Exception in thread "Main" java.lang.StackOverflowError
        //Main(args);

        //堆溢出：Exception in thread "Main" java.lang.OutOfMemoryError: Java heap space
        Integer[] integers = new Integer[1024 * 1024 * 1024];
    }

    public void test() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.format(new Date());
    }
}
