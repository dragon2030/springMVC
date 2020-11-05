package com.bigDragon.javase.exceptionError;

/**
 * 异常处理的方式二：throws+异常类型：
 *      throws + 异常类型"写在方法的声明在方法的声明处，指明此方法执行时，可能会抛出的异常类型。
 *      一旦当方法体执行时，出现异常，仍会在异常代码处生成一个异常类的对象，此对象满足throws后异常类型时，就会抛出，异常代码后续的代码将不执行。
 *
 * 开发中如何选择使用try-catch-finally还是throws?
 *      1.如果父类中被重写的方法中没有throws方法处理异常，则子类重写的方法也不能使用throws，意味着如果子类重写的方法中有异常，必须使用try-catch-finally方法处理。
 *      2.执行的方法a中，先后又调用了另外的几个方法，这几个方法是递进关系的。我们建议这几个方法使用throws的方法进行处理。而执行的方法a可以考虑使用try-catch-finally方法进行处理。
 * @author bigDragon
 * @create 2020-10-23 17:28
 */
public class ThrowsException {
    public static void main(String[] args){
        ThrowsException throwsException = new ThrowsException();
        try {
            throwsException.method1();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    public void method1 ()throws ArrayIndexOutOfBoundsException{
        String[] array=new String[2];
        String st=array[5];
        System.out.println("异常产生之后的代码");
    }
}
