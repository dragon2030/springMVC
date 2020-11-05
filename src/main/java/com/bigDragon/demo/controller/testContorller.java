package com.bigDragon.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.bigDragon.demo.service.TestService;
import com.bigDragon.javase.ioStream.ExcelTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public TestService testService;
	
	@RequestMapping(value = "/main")
	public ModelAndView testJsp(){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("demo");
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

	/**
	 * 逐条事务管理
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/dataDispose")
	@ResponseBody
	public String dataDispose(@RequestBody String json){
		JSONObject jsonObject = JSONObject.parseObject(json);
		String filePath=(String)jsonObject.get("filePath");
		//String filePath = "D:\\disposeExcel.xls";
		ExcelTest excelTest= new ExcelTest();
		List<String>list = excelTest.excelDispose(filePath);
		logger.info("获取文件内容成功，条数："+list.size());
		int successNum = 0;
		for (String str:list){
			int result = testService.dataDispose(str);
			if (result == 1)
				successNum++;
		}
		logger.info("总条数："+list.size()+" 成功插入："+successNum);
		return "总条数："+list.size()+" 成功插入："+successNum;
	}

	/**
	 * 整体事务管理
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/dataDispose2")
	@ResponseBody
	public String dataDispose2(@RequestBody String json){
		JSONObject jsonObject = JSONObject.parseObject(json);
		String filePath=(String)jsonObject.get("filePath");
		//String filePath = "D:\\disposeExcel.xls";
		ExcelTest excelTest= new ExcelTest();
		List<String>list = excelTest.excelDispose(filePath);
		logger.info("获取文件内容成功，条数："+list.size());
		int successNum = testService.dataDispose2(list);
		logger.info("总条数："+list.size()+" 成功插入："+successNum);
		return "总条数："+list.size()+" 成功插入："+successNum;
	}

	@RequestMapping(value = "/getUsers")
	@ResponseBody
	public String getUsers(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("users",testService.getUserMsg());
		return JSON.toJSONString(jsonObject);
	}
}
