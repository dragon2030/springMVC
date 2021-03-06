package com.bigDragon.regular;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bigDragon
 * @create 2020-06-05 16:48
 */
public class RegularExpression {
	
    public static void main(String[] args){
        RegularExpression regularExpression=new RegularExpression();

        //String regex = "\\s*(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|17[0-9]|18[0|1|2|3|5|6|7|8|9]|19[0-9])\\d{8}\\s*";
        String regex = "\\d{11}";
        String str=" 15372050554   15372050553";
        //String str=null;//注:当字符串为null时，直接使用matcher就会报错

        //测试字符串是否符合正则表达式格式
        Boolean aBoolean=regularExpression.match(regex, str);
        System.out.println(aBoolean);
        //获取字符串中匹配正则表达式的字符串返回(单次)
        String str2=regularExpression.matchPrintOut(regex,str);
        System.out.println(str2);
/*        String str3=regularExpression.matchPrintOutMulti(regex,str);
        System.out.println(str3);
        
        //获取字符串中指定字符串之后的值
        String st3="梦网平台更新短信状态数据（接收回执方式）：SmsMwStatusReceive [mtmsgid=-8480765987778456652, mtstat=DELIVRD, msgid=2008201936157132954, sa=13148389666, sendStatus=4]";
        String st4 = "梦网平台更新短信状态数据（接收回执方式）：SmsMwStatusReceive ";
        String st5 = regularExpression.matchPrintOut3(st3,st4);*/
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
    

}
