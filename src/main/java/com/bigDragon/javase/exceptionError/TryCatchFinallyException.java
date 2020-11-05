package com.bigDragon.javase.exceptionError;

/**
 *   异常处理的方式一：try-catch-finally的使用
 *      try{
 *          //可能出现异常的代码
 *      }catch(异常类型1 变量名1){
 *          //处理异常的方式1
 *      }catch(异常类型2 变量名2){
 *          //处理异常的方式2
 *      }catch(异常类型2 变量名2){
 *         //处理异常的方式2
 *      }
 *      ....
 *      finally{
 *          //一定会执行的代码
 *      }
 *   过程说明：
 *   1.finally是可选的。
 *   2.使用try将可能出现异常代码包装起来，在执行过程中，一旦出现异常，就会生成一个对应异常类的对象，根据对象的类型，去catch中进行匹配。
 *   3.一旦try中的异常对象匹配到一个catch时，就进入catch中进行异常的处理，一旦处理完成，就跳出当前的try-catch结构（在没有写finally的情况下），继续执行其后的代码
 *   4.catch中的异常类型如果没有子父类关系，则谁声明在上谁声明在下无所谓
 *     catch中的异常类型如果满足子父关系，则要求子类一定声明在父类的上面，否则报错。
 *   5.常用的异常对象处理方式：
 *      String getMessage()
 *      printStackTrack()
 *   6.在try结构中声明的变量，再出了try结构以后，就不能在被调用
 *
 *   辨析finally与直接在try-catch后写的区别：
 *      当catch中产生异常且没有被捕获时，程序会中止不会执行try-catch后的代码，而finally始终会执行
 *
 *   finally解析：
 *      1.finally是可选的
 *      2.finally中声明的是一定会被执行的代码。即使catch中又出现了异常了，try中又return语句，catch中又return语句等情况。
 *      3.想数据库连接、输入输出流、忘了变成Socket等资源。JVM是不能自动的回收的，我们需要自己手动的进行资源的释放，此时的资源释放，就需要声明finally中。
 *
 * @author bigDragon
 * @create 2020-10-22 18:42
 */
public class TryCatchFinallyException {
    public static void main(String[] args){
        TryCatchFinallyException tryCatchFinally = new TryCatchFinallyException();
        //辨析finally与直接在try-catch后写的区别
        tryCatchFinally.finallyTest();
    }

    /**
     * 辨析finally与直接在try-catch后写的区别
     */
    public void finallyTest(){
        try {
            String st= new String("123");
            st.substring(5);
        } catch (Exception e) {
            String st= new String("123");
            st.substring(5);
            System.out.println("catch执行");
            e.printStackTrace();
        } finally {
            System.out.println("finally执行");
        }
        System.out.println("try-catch之后的逻辑执行");
    }
}
