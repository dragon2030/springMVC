package com.bigDragon.WebService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * ajax传输测试
 * @author bigDragon
 * @create 2020-06-15 19:23
 */
@Controller
@RequestMapping("/ajaxTest")
public class AjaxTest {
    private static final Logger logger = LoggerFactory.getLogger(AjaxTest.class);

    /**
     * 获取ajax请求中HttpServletRequest，HttpServletResponse中的信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/requestTest")
    @ResponseBody
    public void requestTest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //获得客户机信息
        System.out.println("request.getRequestURL():"+request.getRequestURL());//返回客户端发出的请求时的完整URL
        System.out.println("request.getRequestURI():"+request.getRequestURI());//返回请求行中的参数部分
        BufferedReader br = request.getReader();
        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        logger.warn("BODY: "+wholeStr);//返回请求体
        System.out.println("request.getQueryString():"+request.getQueryString());//方法返回请求行中参数部分（参数+值）
        System.out.println("request.getRemoteHost():"+request.getRemoteHost());//返回发出请求的客户机的完整主机名
        System.out.println("request.getRemoteAddr():"+request.getRemoteAddr());//返回发出请求的客户机的IP地址
        System.out.println("request.getPathInfo():"+request.getPathInfo());//返回请求URL的额外路径信息
        System.out.println("request.getRemotePort():"+request.getRemotePort());//返回客户机所使用的网络端口号
        System.out.println("request.getLocalAddr():"+request.getLocalAddr());//返回WEB服务器的IP地址
        System.out.println("request.getLocalName():"+request.getLocalName());//返回WEB服务器的主机名
        //获得客户机请求头
        //System.out.println("request.getHeader:"+request.getHeader(""));//以String的形式返回请求头的值
        //System.out.println("request.getHeaders:"+request.getHeaders(""));//以String的形式返回请求头的所有值
        System.out.println("request.getHeaderNames:"+request.getHeaderNames());//返回此请求包含的所有头名称的枚举
        //获取客户机请求参数
        //System.out.println("request.getParameter:"+request.getParameter(""));//根据name获取请求参数
        System.out.println("request.getParameterValues:"+request.getParameterValues(""));//根据name获取请求参数列表
        System.out.println("request.getParameterMap:"+request.getParameterMap());//返回map类型，所提交请求中的映射关系
        //请求转发
        RequestDispatcher requestDispatcher =request.getServletContext().getRequestDispatcher("/index.jsp");
        requestDispatcher.forward(request,response);
        //作为一个域对象
        request.setAttribute("name","Jack");//添加name属性
        System.out.println("request.getAttribute:"+request.getAttribute("name"));//或许name属性
        request.removeAttribute("name");//移除name属性
        Enumeration<String> enumeration=request.getAttributeNames();//获取所有属性名

        System.out.println("request.getMethod():"+request.getMethod());
        System.out.println("request.getContextPath():"+request.getContextPath());

        String requestUrl=request.getRequestURL().toString();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/html;charset=UTF-8");
        PrintWriter out =response.getWriter();
        out.write("请求的URL："+requestUrl);
    }

    /**
     * 测试页面跳转
     * @return
     */
    @RequestMapping("/test1")
    public ModelAndView test1(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("ajaxTest");
        return modelAndView;
    }
    /**
     * 测试页面跳转2
     * @return
     */
    @RequestMapping("/test2")
    public ModelAndView test2(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("ajaxTest2");
        return modelAndView;
    }

    @RequestMapping("/test4")
    @ResponseBody
    public String test4(String param){
        logger.info("get param:"+param);
        return param;
    }

    /**
     * 测试返回json对象
     * contentType:"application/json"时
     * data:{"param":"value"}
     * @param param
     * @return
     */
    @RequestMapping("/test5")
    @ResponseBody
    public String test5(@RequestBody String param){
        List<User> list=new ArrayList<User>();
        User user=new User();
        user.setAge("21");
        user.setName("Mike");
        list.add(user);
        User user2=new User();
        user2.setAge("22");
        user2.setName("Sam");
        list.add(user2);
        return JSON.toJSONString(list);
    }

    /**
     * ajax测试页面
     * @return
     */
    @RequestMapping("/view1")
    public ModelAndView testAjax(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("ajaxTest");
        return modelAndView;
    }

    @RequestMapping(value = "/ajaxtest1")
    @ResponseBody
    public String ajaxtest1(@RequestBody String json){
        logger.info(json);
/*        List users = new ArrayList<User>();
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
        return JSON.toJSONString(jsonObject);*/
        return "123";
    }
}
