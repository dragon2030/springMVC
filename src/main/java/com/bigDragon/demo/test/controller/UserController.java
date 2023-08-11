package com.bigDragon.demo.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.demo.test.entity.User;
import com.bigDragon.demo.test.service.IUserService;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/7/14
 * @Description:
 */
@RestController
public class UserController {

    @Resource
    IUserService userService;

    @RequestMapping(value = "/getUsers",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsers(){
        List<User> users = userService.selectAll();
        return users;
    }
}
