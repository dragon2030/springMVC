package com.bigDragon.WebService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author bigDragon
 * @create 2020-06-17 14:07
 *
 * @RequestParam：将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）
 *
 * 语法：@RequestParam(value=”参数名”,required=”true/false”,defaultValue=””)
 * value：参数名
 * required：是否包含该参数，默认为true，表示该请求路径中必须包含该参数，如果不包含就报错。
 * defaultValue：默认参数值，如果设置了该值，required=true将失效，自动为false,如果没有传该参数，就使用默认值
 */
@Controller
@RequestMapping("/requestParam")
public class RequestParamController {

    /**
     * http://localhost:8082/spring_boot/requestParam/test1?param=ABC 返回：接收普通的请求参数ABC
     * 参数必传 无该参数返回400 Bad Request
     * 参数设置为必传
     * @param param
     * @return
     */
    @RequestMapping("/test1")
    @ResponseBody
    public ModelAndView test1(@RequestParam String param){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("msg","接收普通的请求参数"+param);
        return modelAndView;
    }

    /**
     * http://localhost:8082/spring_boot/requestParam/test1?param=ABC 返回：接收普通的请求参数ABC
     * http://localhost:8082/spring_boot/requestParam/test1 返回：接收普通的请求参数null
     * 参数非必传
     * @param param
     * @return
     */
    @RequestMapping("/test2")
    @ResponseBody
    public ModelAndView test2(@RequestParam(value = "param",required = false) String param){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("msg","接收普通的请求参数"+param);
        return modelAndView;
    }

    /**
     * http://localhost:8082/spring_boot/requestParam/test3?param=ABC 返回：接收普通的请求参数ABC
     * http://localhost:8082/spring_boot/requestParam/test1 返回：接收普通的请求参数123
     * 参数非必传设置默认值
     * @param param
     * @return
     */
    @RequestMapping("/test3")
    @ResponseBody
    public ModelAndView test3(@RequestParam(value = "param",required = true,defaultValue = "123") String param){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("msg","接收普通的请求参数"+param);
        return modelAndView;
    }

    /**
     * http://localhost:8082/spring_boot/requestParam/test3?param=ABC 返回：接收普通的请求参数ABC
     * http://localhost:8082/spring_boot/requestParam/test1 返回：接收普通的请求参数null
     * 效果等同于@RequestParam(value = "param",required = false)
     * @param param
     * @return
     */
    @RequestMapping("/test4")
    @ResponseBody
    public ModelAndView test4(String param){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("msg","接收普通的请求参数"+param);
        return modelAndView;
    }

    @RequestMapping("/test5")
    @ResponseBody
    public ModelAndView test5(String name,String age){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("msg","接收普通的请求参数"+name+"|"+age);
        return modelAndView;
    }

}
