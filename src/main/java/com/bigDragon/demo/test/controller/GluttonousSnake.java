package com.bigDragon.demo.test.controller;

import com.bigDragon.common.CommonValue;
import com.bigDragon.demo.test.entity.GluttonousSnakeLengthRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author bigDragon
 * @create 2020-06-02 15:28
 */
@Controller
@RequestMapping("/gluttonousSnake")
public class GluttonousSnake {
    private static final Logger logger = LoggerFactory.getLogger(GluttonousSnake.class);

    @RequestMapping("/gluttonousSnake1.0")
    public String show_gluttonous_snake() {
        return "GluttonousSnake1.0";
    }

    @RequestMapping("/snakeDeath")
    public ModelAndView snakeDeathView(@RequestParam(value = "length" ) int length,
                                       HttpServletRequest request, HttpServletResponse response){
        //创建当前贪吃蛇死亡记录对象
        GluttonousSnakeLengthRecord gluttonousSnakeLengthRecord
                = new GluttonousSnakeLengthRecord();
        gluttonousSnakeLengthRecord.setId(UUID.randomUUID().toString());
        gluttonousSnakeLengthRecord.setLength(length);
        gluttonousSnakeLengthRecord.setTime(new Date());
        gluttonousSnakeLengthRecord.setColorFlag("1");
        logger.info("本次贪吃蛇记录为："+gluttonousSnakeLengthRecord);
        ModelAndView modelAndView=new ModelAndView();
        List<GluttonousSnakeLengthRecord> list = new LinkedList<GluttonousSnakeLengthRecord>();
        //添加进贪吃蛇死亡记录链表
        synchronized (this){
            int index = lengthRecordAddIndex(CommonValue.snakeLengthRecords,CommonValue.snakeLengthRecords.size()-1,length);
            CommonValue.snakeLengthRecords.add(index,gluttonousSnakeLengthRecord);
            if(CommonValue.snakeLengthRecords.size()>CommonValue.snakeRankingLength)
                CommonValue.snakeLengthRecords.remove(CommonValue.snakeLengthRecords.size()-1);
            logger.info("贪吃蛇死亡记录链表: "+CommonValue.snakeLengthRecords.toString());
            //集合浅拷贝
            for(GluttonousSnakeLengthRecord g:CommonValue.snakeLengthRecords){
                list.add((GluttonousSnakeLengthRecord)g.clone());
            }
            //清除贪吃蛇死亡记录链表中颜色标志位
            CommonValue.snakeLengthRecords.get(index).setColorFlag(null);
        }
        modelAndView.setViewName("snakeDeath");//跳转页面
        modelAndView.addObject("length", length);
        modelAndView.addObject("list", list);
        return modelAndView;
    }

    /**
     * 查找当前贪吃蛇长度在贪吃蛇死亡记录链表中按照长度，时间排序后插入的指针位置
     * @param list
     * @param index
     * @param length
     * @return
     */
    public  int lengthRecordAddIndex(List<GluttonousSnakeLengthRecord> list,int index,int length){
        if(index == -1){//贪吃蛇死亡记录链表长度为0时，直接添加到0指针
            return index+1;
        }else if(list.get(index).getLength() >= length){
            return index+1;
        }else if(list.get(index).getLength() < length && index!=0){
            return lengthRecordAddIndex(list,index-1,length);
        }else {
            return 0;
        }
    }
}
