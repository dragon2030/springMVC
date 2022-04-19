package com.bigDragon.jsonParse;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author bigDragon
 * @create 2020-09-07 9:35
 */
@Controller
@RequestMapping("/jsParseJson")
public class JSParseJson {
    private static final Logger logger = LoggerFactory.getLogger(JSParseJson.class);

    @RequestMapping(value = "/test")
    public ModelAndView testJsp(){
        logger.info("log4j正常打印日志信息");
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("jsonParse");
        logger.info("log4j正常打印日志信息2");
        return modelAndView;
    }

    /**
     * list插入json中
     * @param json
     * @return
     */
    @RequestMapping(value = "/ajaxTest")
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
