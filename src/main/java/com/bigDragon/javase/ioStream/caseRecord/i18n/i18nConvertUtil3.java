package com.bigDragon.javase.ioStream.caseRecord.i18n;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//国际化转换-excel
public class i18nConvertUtil3 {
    public static void main(String[] args) {
//        String str = "NO_CMD_DETAIL(0, \"\", \"\"),";
//        String regex = "\"([^\"]*)\"";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(str);
//        if (matcher.find()) {
//            System.out.println(matcher.group(1));; // 返回第一个括号内匹配的内容，即enum或class之后的名称
//            System.out.println(matcher.matches());
//        }
        String[] titleNames = new String[] {"表号","省","市","县","小区名称","燃气公司代码","用户名","用户号","用户地址","接入平台类型（1-NB-Iot 2-OneNet 0-IotCloud）","表具类型(1-民用膜式表 2-工业膜式表 3-工业流量计)"};
        for(String str : titleNames){
            System.out.println(str+":"+str);
        }
    }
    //适用于
    //String[] titleNames = new String[] {"表号","省","市","县","小区名称","燃气公司代码","用户名","用户号","用户地址","接入平台类型（1-NB-Iot 2-OneNet 0-IotCloud）","表具类型(1-民用膜式表 2-工业膜式表 3-工业流量计)"};
    //Workbook workbook = ExportExcelUtil.createWorkbook(ConstantsManagement.NAME_GMBASE_EXCELTEMPLATE, titleNames);
    @Test
    public void Test1(){
        String[] titleNames = new String[] {"表号","省","市","县","小区名称","燃气公司代码","用户名","用户号","用户地址","接入平台类型（1-NB-Iot 2-OneNet 0-IotCloud）","表具类型(1-民用膜式表 2-工业膜式表 3-工业流量计)"};
        for(String str : titleNames){
            System.out.println(str+"="+str);
        }
    }
}
