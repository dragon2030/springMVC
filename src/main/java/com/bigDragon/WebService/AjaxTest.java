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
    @RequestMapping("/ajaxStandard")
    @ResponseBody
    public String ajaxStandard(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        StringBuffer stringBuffer = new StringBuffer();
        //获得客户机信息
        stringBuffer.append("request.getRequestURL():").append(request.getRequestURL()).append("\n");//返回客户端发出的请求时的完整URL
        stringBuffer.append("request.getRequestURI():").append(request.getRequestURI()).append("\n");//返回请求行中的参数部分
        BufferedReader br = request.getReader();
        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        stringBuffer.append("BODY: ").append(wholeStr).append("\n");//返回请求体
        stringBuffer.append("request.getQueryString():").append(request.getQueryString()).append("\n");//方法返回请求行中参数部分（参数+值）
        stringBuffer.append("request.getRemoteHost():").append(request.getRemoteHost()).append("\n");//返回发出请求的客户机的完整主机名
        stringBuffer.append("request.getRemoteAddr():").append(request.getRemoteAddr()).append("\n");//返回发出请求的客户机的IP地址
        stringBuffer.append("request.getPathInfo():").append(request.getPathInfo()).append("\n");//返回请求URL的额外路径信息
        stringBuffer.append("request.getRemotePort():").append(request.getRemotePort()).append("\n");//返回客户机所使用的网络端口号
        stringBuffer.append("request.getLocalAddr():").append(request.getLocalAddr()).append("\n");//返回WEB服务器的IP地址
        stringBuffer.append("request.getLocalName():").append(request.getLocalName()).append("\n");//返回WEB服务器的主机名
        //获得客户机请求头
        stringBuffer.append("request.getHeader:").append(request.getHeader("")).append("\n");//以String的形式返回请求头的值
        stringBuffer.append("request.getHeaders:").append(request.getHeaders("")).append("\n");//以String的形式返回请求头的所有值
        stringBuffer.append("request.getHeaderNames:").append(request.getHeaderNames()).append("\n");//返回此请求包含的所有头名称的枚举
        //获取客户机请求参数
        stringBuffer.append("request.getParameter:").append(request.getParameter("")).append("\n");//根据name获取请求参数
        stringBuffer.append("request.getParameterValues:").append(request.getParameterValues("")).append("\n");//根据name获取请求参数列表
        stringBuffer.append("request.getParameterMap:").append(request.getParameterMap()).append("\n");//返回map类型，所提交请求中的映射关系
        //请求转发
        //RequestDispatcher requestDispatcher =request.getServletContext().getRequestDispatcher("/index.jsp");
        //requestDispatcher.forward(request,response);
        //作为一个域对象
        request.setAttribute("name","Jack");//添加name属性
        System.out.println("request.getAttribute:"+request.getAttribute("name"));//或许name属性
        request.removeAttribute("name");//移除name属性
        Enumeration<String> enumeration=request.getAttributeNames();//获取所有属性名

        stringBuffer.append("request.getMethod():").append(request.getMethod()).append("\n");
        stringBuffer.append("request.getContextPath():").append(request.getContextPath()).append("\n");

        logger.info(stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 页面跳转
     * @return
     */
    @RequestMapping("/main")
    public ModelAndView test1(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("ajaxTest");
        return modelAndView;
    }


    @RequestMapping(value = "/ajaxError")
    @ResponseBody
    public String ajaxError(@RequestBody String json){
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
     * ajax中dataType
     * 默认类型是：String
     *
     * ajax本身会自动推测服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断，比如 XML MIME 类型就被识别为 XML。在 1.4 中，JSON 就会生成一个 JavaScript 对象，而 script
     * 则会执行这个脚本。随后服务器端返回的数据会根据这个值解析后，传递给回调函数。可用值:
     *
     * •”xml”: 返回 XML 文档，可用 jQuery 处理。
     * •”html”: 返回纯文本 HTML 信息；包含的 script 标签会在插入 dom 时执行。
     * •”script”: 返回纯文本 JavaScript 代码。不会自动缓存结果。除非设置了 “cache” 参数。注意：在远程请求时(不在同一个域下)，所有 POST 请求都将转为 GET 请求。（因为将使用 DOM 的 script标签来加载）
     * •”json”: 返回 JSON 数据 。
     * •”jsonp”: JSONP 格式。使用 JSONP 形式调用函数时，如 “myurl?callback=?” jQuery 将自动替换 ? 为正确的函数名，以执行回调函数。
     * •”text”: 返回纯文本字符串
     * @return
     */
    @RequestMapping(value = "/ajaxDataType")
    @ResponseBody
    public String ajaxDataType(){
        List users = new ArrayList<User>();
        User user = new User();
        user.setName("Jack");
        user.setAge("25");
        user.setPeopleDes("very nice");
        user.setSexId("1");
        users.add(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", users);
        logger.info(JSON.toJSONString(jsonObject));
        return JSON.toJSONString(jsonObject);
    }

    @RequestMapping(value = "/ajaxTest")
    @ResponseBody
    public String ajaxTest(@RequestBody String json){
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
