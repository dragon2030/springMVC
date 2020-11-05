package com.bigDragon.javase.concurrent.createThread;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

/**
 * spring集成线程池 
 * @author: bigDragon
 * @date: 2020年8月13日
 *
 */
@Controller
@RequestMapping("/threadPool")
public class ThreadPoolDemo {

	@Resource(name="threadPool")
    private ThreadPoolTaskExecutor threadPool;
	
	@Resource(name="threadPool2")
    private ThreadPoolTaskExecutor threadPool2;
	
	
	/**
	 * 创建线程池
	 * 请求地址 http://ip:port/bigDragon/threadPool/testThreadPool
	 * 请求样式 POST application/json {"sleepTime":"3","circleNum":"5"}
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/testThreadPool")
	@ResponseBody
	public String testThreadPool(@RequestBody String json){
		try{
	        JSONObject jsonObject=JSONObject.parseObject(json);
	        String sleepTime=jsonObject.get("sleepTime").toString();
	        int circleNum=jsonObject.get("circleNum")!=null?Integer.parseInt(jsonObject.get("circleNum").toString()):0;
	        if(circleNum==0){
	            MyThread myThread3=new MyThread(sleepTime);
	            threadPool.execute(myThread3);
	        }else{
	        	for(int i=0;i<circleNum;i++){
	                MyThread myThread3=new MyThread(sleepTime);
	                threadPool.execute(myThread3);
	        	}
	        }
			return "SUCCESS";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ERROR";
	}
	@RequestMapping("/hello")
	public ModelAndView hello(@RequestBody String json){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("demo");
		return modelAndView;
	}
	/**
	 * 获取线程池状态
	 * 请求地址 http://ip:port/bigDragon/threadPool/currentThreadPool
	 * 请求样式 GET
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/currentThreadPool")
	@ResponseBody
	public String currentThreadPool(){
		StringBuffer s=new StringBuffer();
		s.append("获取当前线程池的线程数量:").append(threadPool.getPoolSize()).append("\n");
		s.append("获取活动的线程的数量:").append(threadPool.getActiveCount()).append("\n");
		return s.toString();
	}
	
	@RequestMapping(value = "/createThread")
	@ResponseBody
	public String createThread(){
		try{
			MyThread myThread3=new MyThread("1");
			new Thread(myThread3).start();
			return "create thread success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "create thread error";
	}
	
	/**
	 * 创建线程池
	 * 请求地址 http://ip:port/bigDragon/threadPool/testThreadPool2
	 * 请求样式 POST application/json {"sleepTime":"3","circleNum":"5"}
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/testThreadPool2")
	@ResponseBody
	public String testThreadPool2(@RequestBody String json){
		try{
	        JSONObject jsonObject=JSONObject.parseObject(json);
	        String sleepTime=jsonObject.get("sleepTime").toString();
	        int circleNum=jsonObject.get("circleNum")!=null?Integer.parseInt(jsonObject.get("circleNum").toString()):0;
	        if(circleNum==0){
	            MyThread myThread3=new MyThread(sleepTime);
	            threadPool2.execute(myThread3);
	        }else{
	        	for(int i=0;i<circleNum;i++){
	                MyThread myThread3=new MyThread(sleepTime);
	                threadPool2.execute(myThread3);
	        	}
	        }
			return "SUCCESS";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ERROR";
	}
	/**
	 * 获取线程池状态
	 * 请求地址 http://ip:port/bigDragon/threadPool/currentThreadPool
	 * 请求样式 GET
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/currentThreadPool2")
	@ResponseBody
	public String currentThreadPool2(){
		StringBuffer s=new StringBuffer();
		s.append("获取当前线程池的线程数量:").append(threadPool2.getPoolSize()).append("\n");
		s.append("获取活动的线程的数量:").append(threadPool2.getActiveCount()).append("\n");
		return s.toString();
	}
}