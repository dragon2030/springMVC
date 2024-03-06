package com.bigDragon.javase.ioStream.caseRecord.i18n;

import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import com.bigDragon.util.RegularUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 国际化转换-枚举类
 */
public class i18nConvertUtil2 {

    public static void main(String[] args) {
        i18nConvertUtil2 main = new i18nConvertUtil2();
//        System.out.println("******************************************************************");
        //获取源数据
        String originContent = CommonUtil.readerTemplate("D:\\io_test\\i18n\\originContent_enum.txt");
//        System.out.println("******************************************************************");

        //组装成需执行的sql
        main.dateProcess(originContent);

    }


    //核心处理方法-新
    //只生成国际化配置文件
    public void dateProcess(String originContent) {
        StringBuffer afterProcessCode = new StringBuffer();//返回修改后的原异常文件
        StringBuffer i18nContent = new StringBuffer();//返回的i18n配置文件
        //两个文件一个是代码输出文件，一个是国际化输出文件
        String[] split = originContent.split("\r\n");//按照换行符进行逐行分割
        int group = 1;


        String regex1 = "public (enum|class) (\\w+)\\s*\\{";//用于获取类名
        String regex = "\"([^\"]+)\"";
        // String regex = "\"([^\"]*)\"";//有一个如果为空值的问题，这个问题只能先手动解决，不然原文件不好修改进去，这种情况也不多
        //String regex = "[A-Z_]+\\((-?\\d+),\\s*\"([^"]+)\"(?:,\\s*\"([^"]+)\")?\\)";
        String className = null;//类名称

//        afterProcessCode.append("//国际化改造").append("\r\n");
        for (String text : split) {
            String chinaValue = null;

            if(RegularUtil.matcher(regex1, text)){
                className = RegularUtil.patternMatcher(regex1, text,2);
                System.out.println("获取到className"+className);
                i18nContent.append("# enum "+className).append("\r\n");
            }

            chinaValue = RegularUtil.patternMatcher(regex, text,group);

            if(StringUtils.isBlank(chinaValue)){
                afterProcessCode.append(text).append("\r\n");
            }else{
                //修改后原文件
                afterProcessCode.append("//").append(text).append("\r\n");//原本数据变成备注保存
                String replaceChinaValue = className+"_"+chinaValue;
                String newText = text.replace(chinaValue,replaceChinaValue);
                afterProcessCode.append(newText).append("\r\n");
                i18nContent.append(replaceChinaValue).append("=").append(chinaValue).append("\r\n");
            }
        }
        CommonUtil.writerOut(afterProcessCode.toString(),"D:\\io_test\\i18n\\originContent_enum_afterProcessCode.txt");
        CommonUtil.writerOut(i18nContent.toString(),"D:\\io_test\\i18n\\i18nContent_enum.txt",true);
    }



}
