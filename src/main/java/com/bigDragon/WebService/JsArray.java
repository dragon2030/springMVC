package com.bigDragon.WebService;

import com.bigDragon.demo.controller.testContorller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * js对数组的操作运算
 * @author bigDragon
 * @create 2020-09-08 10:29
 */
@Controller
@RequestMapping("/jsArray")
public class JsArray {
    private static final Logger logger = LoggerFactory.getLogger(JsArray.class);

    @RequestMapping(value = "/main")
    public ModelAndView testJsp(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("jsArray");
        return modelAndView;
    }
}
