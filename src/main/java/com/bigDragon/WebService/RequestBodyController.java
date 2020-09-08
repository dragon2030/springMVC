package com.bigDragon.WebService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author bigDragon
 * @create 2020-06-17 18:45
 *
 * @RequestBody 接收的是请求体里面的数据
 * 后端参数可以为一个对象
 * 实体类的对应属性的类型要求时,会调用实体类的setter方法将值赋给该属性。
 * json字符串中，如果value为""的话，后端对应属性如果是String类型的，那么接受到的就是""，如果是后端属性的类型是Integer、Double等类型，那么接收到的就是null。
 * 后端可以为一个参数
 */
@Controller
@RequestMapping("/requestBody")
public class RequestBodyController {

    /**
     * postman测试
     * POST请求请求体：{"param":"ABC"} 请求头：Content-Type:application/json
     * @param json
     * @return
     */
    @RequestMapping("/test1")
    public ModelAndView test1(@RequestBody String json){
        JSONObject jsonObject=JSONObject.parseObject(json);
        String param=String.valueOf(jsonObject.get("param"));
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("msg","接收普通的请求参数"+param);
        return modelAndView;
    }

    /**
     * 请求格式为json字符串 例：{"name":"ABC","age":"22"}
     * contentType: "application/json;charset=UTF-8"
     * @param user
     * @return
     */
    @RequestMapping("/test2")
    @ResponseBody
    public JSONObject test2(@RequestBody User user){
        String name=user.getName();
        String age=user.getAge();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("age",age);
        System.out.println(JSON.toJSONString(jsonObject));
        return jsonObject;
    }

    @RequestMapping("/test3")
    public void test3(@RequestBody String json){
        System.out.println(json);
    }

}
