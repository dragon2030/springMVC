package com.bigDragon.javase.ioStream.caseRecord.i18n;



import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import com.bigDragon.javase.ioStream.caseRecord.i18n.TranslateApi;
import com.bigDragon.util.RegularUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 国际化转换-异常类
 */
public class i18nConvertUtil {

    /**
     * /**
     *
     * @author: bigDragon
     * @create: 2022/9/2
     * @Description: 用于写出需要执行的sql后在所有ibs上线的子公司上执行sql代码
     * 因为数据库不同，需要改变其sql内分公司名称
     * 魔板样例与doc/io_demo
     */
    public static void main(String[] args) {
        i18nConvertUtil main = new i18nConvertUtil();
        System.out.println("******************************************************************");
        //获取源数据
        String originContent = CommonUtil.readerTemplate("D:\\io_test\\originContent.txt");
        System.out.println("******************************************************************");

        //组装成需执行的sql
        main.dateProcess2(originContent);
//        String string = TranslateApi.translateApi("中继器编号重复");
//        System.out.println(string);
    }


    /**
     * 核心处理方法-原
     * 修改原文件并修生成新的配置文件，效果如下
     * //	@ExceptionInfo(key = "109000", value = "表号不存在")
     *        @ExceptionInfo(key = "109000", value = "COMMON_MESS_109000")
     * 	public static final String COMMON_MESS_109000 = "109000";
     */

    public void dateProcess(String originContent) {
        StringBuffer afterProcessCode = new StringBuffer();//返回修改后的原异常文件
        StringBuffer i18nContent = new StringBuffer();//返回的i18n配置文件
        //两个文件一个是代码输出文件，一个是国际化输出文件
        String[] split = originContent.split("\r\n");


        String lastRow = null;
        String lastChinaValue =null;
        for (String text : split) {
            String chinaValue = null;//当前行的中文值
            String englishValue = null;//当前行的英文值

            // 正则表达式
            //\s	匹配任何空白字符，包括空格、制表符、换页符等等。注意：如果没有这个捕获组，会返回value = "some_value"整个信息
            //[^\"]+: 匹配一个或多个非双引号字符
            //(: 开始一个捕获组，用于捕获匹配的部分。): 结束捕获组。
            String regex = "value\\s*=\\s*\"([^\"]+)\"";
            chinaValue = RegularUtil.patternMatcher(regex, text);


            // 正则表达式
            String regex2 = "public static final String\\s*([\\w]+)\\s*=";
            englishValue = RegularUtil.patternMatcher(regex2, text);

            if(StringUtils.isBlank(englishValue)){
                if(StringUtils.isBlank(chinaValue)){//cn no en no直接复制
                    afterProcessCode.append(text).append("\r\n");
                }else{//cn yes en no
                    lastChinaValue = chinaValue;
                }
            }else{//cn no en yes 插入两行 第一行@ExceptionInfo 第二行 public static final String
                if(StringUtils.isBlank(chinaValue)){
                    //第一行
                    System.out.println("lastRow:"+lastRow+" lastChinaValue:"+lastChinaValue+" englishValue:"+englishValue);
                    afterProcessCode.append("//").append(lastRow).append("\r\n");//原本数据变成备注保存
                    String replace = lastRow.replaceAll(lastChinaValue, englishValue);
                    afterProcessCode.append(replace).append("\r\n");//上一行的中文替换成下一行的英文
                    //第二行 直接复制
                    afterProcessCode.append(text).append("\r\n");//这一行保留原文
                    i18nContent.append(englishValue).append("=").append(lastChinaValue).append("\r\n");
                }

            }

            lastRow=text;//最后处理
        }
        CommonUtil.writerOut(afterProcessCode.toString(),"D:\\io_test\\afterProcessCode.txt");
        CommonUtil.writerOut(i18nContent.toString(),"D:\\io_test\\i18nContent.txt");
//        System.out.println(afterProcessCode);
    }

    //核心处理方法-新
    //只生成国际化配置文件
    public void dateProcess2(String originContent) {
        StringBuffer i18nContent = new StringBuffer();//返回的i18n配置文件
        //两个文件一个是代码输出文件，一个是国际化输出文件
        String[] split = originContent.split("\r\n");

        String className = null;//类名称
        String regex1 = "public (enum|class) (\\w+)\\s*\\{";//用于获取类名
        // 正则表达式
        //\s	匹配任何空白字符，包括空格、制表符、换页符等等。
        //[^\"]+: 匹配一个或多个非双引号字符
        //(: 开始一个捕获组，用于捕获匹配的部分。): 结束捕获组。
        String regex = "value\\s*=\\s*\"([^\"]+)\"";

        for (String text : split) {
            String chinaValue = null;

            chinaValue = RegularUtil.patternMatcher(regex, text);//保留类名后续可能使用
            if(RegularUtil.matcher(regex1, text)){
                className = RegularUtil.patternMatcher(regex1, text,2);
                i18nContent.append("# "+className).append("\r\n").append("\r\n");
            }

            if(StringUtils.isNotBlank(chinaValue)){
                i18nContent.append(chinaValue).append("=").append(chinaValue).append("\r\n");
            }
        }
        i18nContent.append("\r\n");
        CommonUtil.writerOut(i18nContent.toString(),"D:\\io_test\\i18nContent.txt");
    }


}
