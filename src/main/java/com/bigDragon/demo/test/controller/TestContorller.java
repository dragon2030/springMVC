package com.bigDragon.demo.test.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;

import com.bigDragon.demo.test.service.TestService;
import com.bigDragon.javase.ioStream.excel.ExcelSimpleDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bigDragon
 */
@Controller
@RequestMapping("/test")
public class TestContorller {
	private static final Logger logger = LoggerFactory.getLogger(TestContorller.class);
	private static String testUpdateStateFlag="1";

	@Autowired
	public TestService testService;



	@RequestMapping(value = "/demo")
	public ModelAndView testJsp(){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("demo");
		return modelAndView;
	}

	@RequestMapping(value = "/testGet")
	public ModelAndView test(){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("test");
		return modelAndView;
	}
	@RequestMapping(value = "/testAjax")
	@ResponseBody
	public String test(Date dateTime){
		System.out.println(dateTime);
		return "success";
	}

	@RequestMapping(value = "/demo2")
	public ModelAndView testJsp2(){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("demo2");
		return modelAndView;
	}

	/**
	 * 结合demo.html，测试不同请求方式的接收url 方法类型 请求体内容
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/demoSubmit")
	@ResponseBody
	public void demoSubmit(HttpServletRequest request, HttpServletResponse response){
		try {
			//**组装请求参数开始

			logger.info("request.getMethod(): "+request.getMethod());
			logger.info("request.getRequestURI(): "+request.getRequestURI());
			logger.info("Character Encoding: " + request.getCharacterEncoding());
			logger.info("HEAD: ");
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				logger.info(key+": "+ value);
			}
			//request.getInputStream() request.getReader() request.getParameter()三方法同时只可用一种
			//getInputStream返回请求内容字节流，多用于文件上传 getReader()是对前者返回内容的封装，可以让调用者更方便字符内容的处理
			BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while((str = br.readLine()) != null){
				wholeStr += str;
			}
			logger.info("BODY: "+wholeStr);


//			String decode = URLDecoder.decode(wholeStr, "utf-8");
//			//请求数据的中文乱码问题
//			request.setCharacterEncoding("UTF-8");//客户端网页我们控制为UTF-8
//			String name = request.getParameter("name");
//			String sex = request.getParameter("sex");
///*			String name = URLDecoder.decode(request.getParameter("name"), "utf-8");
//			String sex = URLDecoder.decode(request.getParameter("sex"), "utf-8");*/
//			String returnVal = "name: "+name+" sex: "+sex;
//			logger.info(returnVal);
//			//获取数据正常，输出数据时可以查阅不同码表
//			response.setCharacterEncoding("gb2312");//通知服务器发送数据时查阅的码表
//			response.setContentType("text/html;charset=gb2312");//通知浏览器以何种码表打开
//			PrintWriter out = response.getWriter();
//			out.write(returnVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//测试结论：get、post方式的编码详解：https://www.cnblogs.com/keyi/p/6365649.html
	//另外tomcat
	@RequestMapping(value = "/demo2_post",method = RequestMethod.POST)
	@ResponseBody
	public void demo2Post(HttpServletRequest request, HttpServletResponse response){
		try {
/*			//**组装请求参数开始

			logger.info("request.getMethod(): "+request.getMethod());
			logger.info("request.getRequestURI(): "+request.getRequestURI());
			logger.info("Character Encoding: " + request.getCharacterEncoding());
			logger.info("HEAD: ");
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				logger.info(key+": "+ value);
			}
			//request.getInputStream() request.getReader() request.getParameter()三方法同时只可用一种
			//getInputStream返回请求内容字节流，多用于文件上传 getReader()是对前者返回内容的封装，可以让调用者更方便字符内容的处理
			BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while((str = br.readLine()) != null){
				wholeStr += str;
			}
			logger.info("BODY: "+wholeStr);


			String decode = URLDecoder.decode(wholeStr, "utf-8");*/
			//请求数据的中文乱码问题
			request.setCharacterEncoding("UTF-8");//客户端网页我们控制为UTF-8
			String name = request.getParameter("name");
			String sex = request.getParameter("sex");
/*			String name = URLDecoder.decode(request.getParameter("name"), "utf-8");
			String sex = URLDecoder.decode(request.getParameter("sex"), "utf-8");*/
			String returnVal = "name: "+name+" sex: "+sex;
			logger.info(returnVal);
			//获取数据正常，输出数据时可以查阅不同码表
			response.setCharacterEncoding("gb2312");//通知服务器发送数据时查阅的码表
			response.setContentType("text/html;charset=gb2312");//通知浏览器以何种码表打开
			PrintWriter out = response.getWriter();
			out.write(returnVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/demo2_get",method = RequestMethod.GET)
	//@ResponseBody
	public void demo2Get(HttpServletRequest request, HttpServletResponse response){
		try {
			//请求数据的中文乱码问题
			//request.setCharacterEncoding("UTF-8");//客户端网页我们控制为UTF-8
			URLDecoder.decode(request.getRequestURI(), "utf-8");
			String name = request.getParameter("name");
			String data = new String (name.getBytes("iso8859-1"),"UTF-8");//解决乱码
			//String data = URLDecoder.decode(name, "utf-8");
			String sex = request.getParameter("sex");
			String returnVal = "name: "+data+" sex: "+sex;
			logger.info(returnVal);
			//获取数据正常，输出数据时可以查阅不同码表
			response.setCharacterEncoding("gb2312");//通知服务器发送数据时查阅的码表
			response.setContentType("text/html;charset=gb2312");//通知浏览器以何种码表打开
			PrintWriter out = response.getWriter();
			out.write(returnVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		ExcelSimpleDemo excelSimpleDemo = new ExcelSimpleDemo();
		List<String>list = excelSimpleDemo.excelDispose(filePath);
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
		ExcelSimpleDemo excelSimpleDemo = new ExcelSimpleDemo();
		List<String>list = excelSimpleDemo.excelDispose(filePath);
		logger.info("获取文件内容成功，条数："+list.size());
		int successNum = testService.dataDispose2(list);
		logger.info("总条数："+list.size()+" 成功插入："+successNum);
		return "总条数："+list.size()+" 成功插入："+successNum;
	}



	@RequestMapping("/success2")
	public String success2(Map<String,Object> map){
		map.put("hello","你好");
		return "success2";
	}

/*	public void testMethod(HttpServletRequest request,HttpServletResponse response){

	}*/
}
