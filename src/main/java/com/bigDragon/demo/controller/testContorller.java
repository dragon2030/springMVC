package com.bigDragon.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.model.User;

@Controller
@RequestMapping("/test")
public class testContorller {
	private static final Logger logger = LoggerFactory.getLogger(testContorller.class); 
	private static String testUpdateStateFlag="1";
	
	@RequestMapping(value = "/main")
	public ModelAndView testJsp(){
    	logger.info("log4j正常打印日志信息");
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("demo");
    	logger.info("log4j正常打印日志信息2");
		return modelAndView;
	}
	
	@RequestMapping(value = "/demo2")
	public ModelAndView testJsp2(){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("demo2");
		return modelAndView;
	}
	
	@RequestMapping(value = "/ajaxtest1")
	@ResponseBody
	public String ajaxtest1(@RequestBody String json){
		logger.info(json);
		List users = new ArrayList<User>();
		User user = new User();
		user.setName("Jack");
		user.setAge("25");
		user.setPeopleDes("very nice");
		user.setSexId("1");
		users.add(user);
		User user2 = new User();
		user2.setName("Sam");
		user2.setAge("26");
		user2.setPeopleDes("very good");
		user2.setSexId("2");
		users.add(user2);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", users);
		logger.info(JSON.toJSONString(jsonObject));
		return JSON.toJSONString(jsonObject);
	}
	
}
