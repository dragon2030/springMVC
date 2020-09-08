package com.bigDragon.thinkOfJava.ThinkOfJavaChapterFour;

import java.util.Random;

public class Main {
	public static void main(String[] args){
		//当nextInt()没有参数时为所有int域内的随机数
        Random randonm = new Random();
        int a = randonm.nextInt();
        System.out.println(a);
        com.bigDragon.thinkOfJava.ThinkOfJavaChapterTwo.practive1.Main.test1();
	}
	
}
