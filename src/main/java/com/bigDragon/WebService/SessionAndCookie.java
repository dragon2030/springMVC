package com.bigDragon.WebService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author bigDragon
 * @create 2020-06-04 9:29
 */
@Controller
@RequestMapping("/sessionAndCookie")
public class SessionAndCookie {
    @RequestMapping(value = "main")
    public ModelAndView JspTest(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("name","Jack");
        modelAndView.setViewName("sessionAndCookie");
        return modelAndView;
    }

    @RequestMapping(value = "formSubmit")
    @ResponseBody
    public String formSubmit(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "message",required = false) String message){
        HttpSession session =request.getSession();
        String result=session.getAttribute("b").toString()!=null?
                session.getAttribute("b").toString():"null";
        return result;
    }

    /**
     * 获取session，key为b的值
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "testSessionMaxInactiveInterval")
    @ResponseBody
    public String testSessionMaxInactiveInterval(HttpServletRequest request, HttpServletResponse response){
        HttpSession session =request.getSession();
        String result=session.getAttribute("b")!=null?
                session.getAttribute("b").toString():"null";
        return result;
    }

    @RequestMapping(value = "main2")
    public void main2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie=new Cookie("myCookie","123");
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        request.getRequestDispatcher("/WEB-INF/sessionAndCookie2.jsp").forward(request, response);
    }
}
