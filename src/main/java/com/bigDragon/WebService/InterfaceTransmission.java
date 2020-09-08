package com.bigDragon.WebService;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.util.HttpClientUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bigDragon
 * @create 2020-08-14 14:42
 */
@Controller
@RequestMapping("/InterfaceTransmission")
public class InterfaceTransmission {
    private static final Logger logger = LoggerFactory.getLogger(InterfaceTransmission.class);

    //测试接口传输方式
    @RequestMapping("demo")
    public void demo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //**组装请求参数开始
        logger.warn("request.getMethod(): "+request.getMethod());
        logger.warn("request.getRequestURI(): "+request.getRequestURI());
        logger.warn("request.getQueryString():"+request.getQueryString());//方法返回请求行中参数部分（参数+值）
        logger.warn("Character Encoding: " + request.getCharacterEncoding());
        logger.warn("HEAD: ");
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            logger.warn(key+": "+ value);
        }
        String str, wholeStr = "";
        if("GET".equalsIgnoreCase(request.getMethod())){
            wholeStr=request.getQueryString();
            logger.warn("Params: "+wholeStr);
        }else if("POST".equalsIgnoreCase(request.getMethod())){
            BufferedReader br = request.getReader();
            while((str = br.readLine()) != null){
                wholeStr += str;
            }
            logger.warn("BODY: "+wholeStr);
        }else{
            logger.warn("非法请求格式： "+request.getMethod());
            return;
        }
        String[] params=wholeStr.split("&");
        Map<String,String> paramsMap=new HashMap<String,String>();
        for(String param:params){
            String[] array=param.split("=");
            String key=array[0];
            String value=array.length<2?"":array[1];
            paramsMap.put(key, value);
        }
    }
    
    @RequestMapping("useGetMethod")
    @ResponseBody
    public String useGetMethod(){
    	JSONObject jsonObject=new JSONObject();
    	jsonObject.put("userid", "1");
    	jsonObject.put("pwd", "12");
    	jsonObject.put("timestamp", "123");
    	jsonObject.put("cmd", "RPT_REQ");
    	jsonObject.put("seqid", "1234");
    	
    	JSONArray jsonArray = new JSONArray();
    	JSONObject jsonObject2=new JSONObject();
    	jsonObject2.put("msgid", "9223372045854775808");
    	jsonObject2.put("custid", "b3d0a2783d31b21b8573");
    	jsonObject2.put("pknum", 1);
    	jsonObject2.put("pktotal", 2);
    	jsonObject2.put("mobile", "15372050554");
    	jsonObject2.put("spno", "1000457890006");
    	jsonObject2.put("exno", "0006");
    	jsonObject2.put("stime", "2016-08-04 17:38:55");
    	jsonObject2.put("rtime", "2016-08-04 17:38:59");
    	jsonObject2.put("status", 0);
    	jsonObject2.put("errcode", "DELIVRD");
    	jsonObject2.put("errdesc", "success");
    	jsonObject2.put("exdata", "exdata0002");
    	jsonArray.add(jsonObject2);
    	
    	jsonObject.put("rpts", jsonArray);
    	String url="localhost:8080/sms-hub/messageProcess/updateStateMW";
    	String param=JSON.toJSONString(jsonObject);
    	System.out.println(param);
    	HttpClientUtil.getInstance().sendHttpGet(url+param);
    	return "success";
    }
    
    
    public static void main(String[] args){
    	InterfaceTransmission interfaceTransmission=new InterfaceTransmission();
    	interfaceTransmission.useGetMethod();
    }
    
}
