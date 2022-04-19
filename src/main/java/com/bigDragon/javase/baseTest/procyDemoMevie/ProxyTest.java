package com.bigDragon.javase.baseTest.procyDemoMevie;

/**
 * 客户类
 * 现在可以看到，代理模式可以在不修改被代理对象的基础上(符合开闭原则)，通过扩展代理类，进行一些功能的附加与增强。值得注意的是，
 * 代理类和被代理类应该共同实现一个接口，或者是共同继承某个类。如前所述，由于Cinema(代理类)是事先编写、编译好的，而不是在程序
 * 运行过程中动态生成的，因此这个例子是一个静态代理的应用。
 * @author bigDragon
 * @create 2021-04-15 9:46
 */
public class ProxyTest {
    public static void main(String[] args) {
        RealMovie realmovie = new RealMovie();
        Movie movie = new Cinema(realmovie);
        movie.play();
    }
    /** Output
     电影马上开始了，爆米花、可乐、口香糖9.8折，快来买啊！
     您正在观看电影 《肖申克的救赎》
     电影马上结束了，爆米花、可乐、口香糖9.8折，买回家吃吧！
     **/
}
