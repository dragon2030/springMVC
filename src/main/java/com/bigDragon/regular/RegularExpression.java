package com.bigDragon.regular;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bigDragon
 * @create 2020-06-05 16:48
 */
public class RegularExpression {

    public static void main(String[] args){
        RegularExpression regularExpression=new RegularExpression();
        String regex = "\\d{0,11}";
        String str="123";

        //测试字符串是否符合正则表达式格式
        Boolean aBoolean=regularExpression.match(regex, str);
        System.out.println(aBoolean);
        //获取字符串中匹配正则表达式的字符串返回(单次)
//        String str2=regularExpression.matchPrintOut(regex,str);
//        System.out.println(str2);

    }

	/**
	 * 测试字符串是否符合正则表达式格式
	 * @param regex	正则表达式
	 * @param str 需要匹配的字符串
	 * @return
	 */
    public Boolean match(String regex,String str){
        if(StringUtils.isNotBlank(str)){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            return m.matches();
        }
        return false;
    }

    /**
     * 获取字符串中匹配正则表达式的字符串返回(单次)
     * 成功匹配返回正确的匹配值，失败或字符串为空返回null
     * @param regex
     * @param str
     * @return
     */
    public String matchPrintOut(String regex,String str){
        if(StringUtils.isNotBlank(str)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            if (m.find()) {
                return m.group();
            }
        }
        return null;
    }

    /**
     * 获取字符串中匹配正则表达式的字符串返回(单次)
     * 成功匹配返回正确的匹配值，失败返回“不符合规范”，字符串为空返回“字符串为空”
     * @param regex
     * @param str
     * @return
     */
    public String matchPrintOut2(String regex,String str){
        if(StringUtils.isNotBlank(str)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            if (m.find()) {
                return m.group();
            }else {
                return"不符合规范";
            }
        }else{
            return "字符串为空";
        }
    }

    /**
     * 获取字符串中匹配正则表达式的字符串返回(多次)
     *
     * @param regex
     * @param str
     * @return
     */
    public String matchPrintOutMulti(String regex,String str){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (m.find()){
        	stringBuffer.append(m.group().trim()).append("\n");
        }
        return stringBuffer.toString();
    }

    /**
     * 获取字符串中指定字符串之后的值
     * @param str
     * @param substr
     * @return
     */
    public String matchPrintOut3(String str,String substr){
        int startIndex=str.indexOf(substr)+substr.length();
        return str.substring(startIndex);
    }

    /**
     * 案例：中文姓名输入匹配
     */
    @Test
    public void case20220107(){
        String input = "郑佳豪";//true
        //String input = "郑佳豪一二三四五六七八九十";//false
        //String input = "dfdsces!";//false
        String regex = "^[\u4e00-\u9fa5_a-zA-Z0-9]{1,10}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        System.out.println(m.matches());
    }

    /**
     * 案例：短信内容输入格式
     * params must be [a-zA-Z0-9] for verification sms
     */
    @Test
    public void test(){
        String str = "aaZZ991";
        boolean matches = str.matches("^[a-zA-Z0-9]+$");
        System.out.println(matches);
    }

}
