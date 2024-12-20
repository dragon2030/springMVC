package com.bigDragon.javase.ioStream.caseRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlScripExecuteBySingle {
    public static void main(String[] args) {
        SqlScripExecuteBySingle main = new SqlScripExecuteBySingle();
        System.out.println("*****************************生成脚本文件程序执行开始*************************************");
        //获取sql模本
        String sqlTemplate = CommonUtil.readerTemplate("D:\\io_test\\sqlScriptTemplate.txt");
        System.out.println("获取sql脚本模本:\n"+sqlTemplate);
        System.out.println("******************************************************************");
        //获取所有参数
        String allParam = CommonUtil.readerTemplate("D:\\io_test\\needParamHouseholdAndMeter.txt");
        System.out.println("获取所有参数:\n"+allParam);
        System.out.println("******************************************************************");
        //组装成需执行的sql
        String executeSql = main.dateProcess(sqlTemplate, allParam);
        System.out.println("组装成需执行的sql:\n"+executeSql.toString());
        //输出
//        main.writerOut(executeSql,"D:\\io_test\\needExecuteSql.txt");
        CommonUtil.writerOut(executeSql,"D:\\io_test\\needExecuteSql.txt",false);
        System.out.println("****************************生成脚本文件程序执行结束**************************************");
    }

    public String dateProcess(String sqlTemplate, String allParam){
        //要输出的字符串
        StringBuffer executeSqlStr = new StringBuffer();
        //去除分公司行间空行
        String[] paramArray = allParam.split("\r\n");
        List<String> paramList = new ArrayList<>(Arrays.asList(paramArray));
        System.out.println("去除分公司行间空行:\n"+paramList);
        System.out.println("******************************************************************");
        for(String paramLine : paramList){
            paramLine = paramLine.trim();
            String[] splitParams = paramLine.split(",");
            if(splitParams.length<2){throw new RuntimeException("参数不足");}
            executeSqlStr.append("-- ").append(paramLine).append("\r\n");
            String newSqlTemplate = sqlTemplate;
            newSqlTemplate = newSqlTemplate.replace("householdNum",splitParams[0]);
            newSqlTemplate = newSqlTemplate.replace("meterNum",splitParams[1]);
            executeSqlStr.append(newSqlTemplate).append("\r\n").append("\r\n");
        }
        return executeSqlStr.toString();
    }
}
