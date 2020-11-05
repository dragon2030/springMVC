package com.bigDragon.javase.concurrent.task;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class TestThreadTask {

	//@Scheduled(cron = "0/10 * * * * ? ")
	 public void taskCycle(){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("spring定时任务执行 "+simpleDateFormat.format(new Date()));
	}
}
