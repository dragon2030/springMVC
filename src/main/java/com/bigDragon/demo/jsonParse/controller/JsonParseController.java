package com.bigDragon.demo.jsonParse.controller;

import com.bigDragon.demo.jsonParse.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 */
@Controller
@RequestMapping("/jsonParse")
@Slf4j
public class JsonParseController {
    @PostMapping("/jackson/jsonFormat_Annotation")
    @ResponseBody
    public User jackson_jsonFormat_Annotation(@RequestBody User user) {
        log.info("user:"+user.toString());
        return user;
    }
}
