package com.bigDragon.javase.ioStream.caseRecord;

import com.bigDragon.util.SnowFlakeUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author: bigDragon
 * @create: 2022/11/15
 * @Description:
 *  业务背景 新增菜单语句固定，需要添加所有角色的的使用权限
 */
public class RoleMenuBindingMappingSql {
    public static void main(String[] args) {
        RoleMenuBindingMappingSql main = new RoleMenuBindingMappingSql();

        //获取sql模本
        String sqlPath="C:\\Users\\bigDragon\\Desktop\\work\\先锋\\sql导出\\20221115\\sql2.txt";
        String sqlTemplate = main.readerSqlTemplate(sqlPath);
        System.out.println("获取sql模本:\n"+sqlTemplate);
        System.out.println("*********************************");
        //获取所有装载参数列表
        String paramPath="C:\\Users\\bigDragon\\Desktop\\work\\先锋\\sql导出\\20221115\\role_id.txt";
        String allCompany = main.readerAllParam(paramPath);
        System.out.println("获取所有分公司:"+allCompany);
        System.out.println("*********************************");
        //组装成需执行的sql
        String executeSql = main.dateProcess(sqlTemplate, allCompany);
        System.out.println("组装成需执行的sql:"+executeSql.toString());
        //输出
        String writerOutPath="C:\\Users\\bigDragon\\Desktop\\work\\先锋\\sql导出\\20221115\\executeSql.txt";
        main.writerOut(writerOutPath,executeSql);
    }

    public String readerSqlTemplate(String pathname) {
        FileReader fr = null;
        StringBuffer sqlTemplate = new StringBuffer();
        try {
            File file = new File(pathname);
            fr = new FileReader(file);

            char[] cbuffer = new char[1024];
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                String str = new String(cbuffer, 0, len);
                sqlTemplate.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //4.资源的关闭
                if (fr != null)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return sqlTemplate.toString();
    }

    public String readerAllParam(String pathname) {
        FileReader fr = null;
        StringBuffer allParam = new StringBuffer();
        try {
            File file = new File(pathname);
            fr = new FileReader(file);

            char[] cbuffer = new char[1024];
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                String str = new String(cbuffer, 0, len);
                allParam.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //4.资源的关闭
                if (fr != null)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return allParam.toString();
        }
    }

    public String dateProcess(String sqlTemplate, String allParam){

        //要输出的字符串
        StringBuffer executeSql = new StringBuffer();
        //去除分公司行间空行
        String noLineBreak = allParam.replace("\n","");
        noLineBreak = noLineBreak.replace("\r","");
        System.out.println("去除分公司行间空行:"+noLineBreak);
        System.out.println("*********************************");
        String[] split = noLineBreak.split(",");
        //
        for(String param : split){
            String operationParam = param.trim();
            executeSql.append("-- ").append(param).append("\r\n");
            String new_sqlTemplate = sqlTemplate.replace("000000",operationParam);

            String snowflakeStr0 = SnowFlakeUtil.getSnowflakeStr();
            String snowflakeStr1 = SnowFlakeUtil.getSnowflakeStr();
            String snowflakeStr2 = SnowFlakeUtil.getSnowflakeStr();
            String snowflakeStr3 = SnowFlakeUtil.getSnowflakeStr();
            String snowflakeStr4 = SnowFlakeUtil.getSnowflakeStr();
            String snowflakeStr5 = SnowFlakeUtil.getSnowflakeStr();

//            String executeSqlStr = MessageFormat.format(
            String executeSqlStr = String.format(
                    new_sqlTemplate,
                    snowflakeStr0,snowflakeStr1,snowflakeStr2,
                    snowflakeStr3,snowflakeStr4,snowflakeStr5);
            executeSql.append(executeSqlStr).append("\r\n");
        }
        return executeSql.toString();
    }

    public void writerOut(String pathname,String executeSql) {
        FileWriter fileWriter = null;//对原有文件的追加
        try {
            //1.提供File类的对象，指明写出到的文件
            File file = new File(pathname);
            if(file.exists()){
                file.delete();
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, true);
            //3.写出的操作
            fileWriter.write(executeSql);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //4.流资源关闭
            try {
                if(fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
