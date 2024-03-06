package com.bigDragon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//正则表达式工具类-记录公共正则表达式处理
public class RegularUtil {


    public static String patternMatcher(String regex,String text){
        return patternMatcher(regex,text,1);
    }
    //正则表达式-公共方法-捕获第一个匹配的值返回
    public static String patternMatcher(String regex,String text,int group){
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(regex);
        // 创建 matcher 对象
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String value = matcher.group(group);
            // 打印第一个捕获组的内容，即 value 的值
            System.out.println("Value: " + value);
            return value;
        }
        return "";
    }
    public static Boolean matcher(String regex,String text){
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(regex);
        // 创建 matcher 对象
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
