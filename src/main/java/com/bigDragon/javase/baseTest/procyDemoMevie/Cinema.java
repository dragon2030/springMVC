package com.bigDragon.javase.baseTest.procyDemoMevie;

/**
 * 代理角色(代理类)
 * 这个表示真正的影片。它实现了 Movie 接口，play()方法调用时，影片就开始播放。
 *
 * Cinema 就是代理对象，它有一个 play() 方法。不过调用 play() 方法时，它进行了一些相关利益的处理，那就是广告。也就是说，
 * Cinema(代理类) 与 RealMovie(目标类) 都可以播放电影，但是除此之外，Cinema(代理类)还对“播放电影”这个行为进行进一步增强，
 * 即增加了额外的处理，同时不影响RealMovie(目标类)的实现。
 *
 * @author bigDragon
 * @create 2021-04-15 9:43
 */
public class Cinema implements Movie {

    RealMovie movie;
    public Cinema(RealMovie movie) {
        super();
        this.movie = movie;
    }

    @Override
    public void play() {
        guanggao(true);    // 代理类的增强处理
        movie.play();     // 代理类把具体业务委托给目标类，并没有直接实现
        guanggao(false);    // 代理类的增强处理
    }

    public void guanggao(boolean isStart){
        if ( isStart ) {
            System.out.println("电影马上开始了，爆米花、可乐、口香糖9.8折，快来买啊！");
        } else {
            System.out.println("电影马上结束了，爆米花、可乐、口香糖9.8折，买回家吃吧！");
        }
    }
}