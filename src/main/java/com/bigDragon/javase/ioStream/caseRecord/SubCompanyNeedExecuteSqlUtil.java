package com.bigDragon.javase.ioStream.caseRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**

 /**
 * @author: bigDragon
 * @create: 2022/9/2
 * @Description:
 *      用于写出需要执行的sql后在所有ibs上线的子公司上执行sql代码
 *      因为数据库不同，需要改变其sql内分公司名称
 *      魔板样例与doc/io_demo
 */
public class SubCompanyNeedExecuteSqlUtil {
    public static void main(String[] args) {
        SubCompanyNeedExecuteSqlUtil main = new SubCompanyNeedExecuteSqlUtil();

        //获取sql模本
        String sqlTemplate = main.readerSqlTemplate();
        System.out.println("获取sql模本:"+sqlTemplate);
        System.out.println("*********************************");
        //获取所有分公司
        String allCompany = main.readerAllCompany();
        System.out.println("获取所有分公司:"+allCompany);
        System.out.println("*********************************");
        //组装成需执行的sql
        String executeSql = main.dateProcess(sqlTemplate, allCompany);
        System.out.println("组装成需执行的sql:"+executeSql.toString());
        //输出
        main.writerOut(executeSql);
    }

    public String readerSqlTemplate() {
        FileReader fr = null;
        StringBuffer sqlTemplate = new StringBuffer();
        try {
            File file = new File("D:\\io_test\\subcompany_sql.txt");
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

    public String readerAllCompany() {
        FileReader fr = null;
        StringBuffer allCompany = new StringBuffer();
        try {
            File file = new File("D:\\io_test\\all_subcompany.txt");
            fr = new FileReader(file);

            char[] cbuffer = new char[1024];
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                String str = new String(cbuffer, 0, len);
                allCompany.append(str);
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
            return allCompany.toString();
        }
    }

    public String dateProcess(String sqlTemplate, String allCompany){

        //要输出的字符串
        StringBuffer execute_sql = new StringBuffer();
        //去除分公司行间空行
        String noLineBreak = allCompany.replace("\n","");
        noLineBreak = noLineBreak.replace("\r","");
        System.out.println("去除分公司行间空行:"+noLineBreak);
        System.out.println("*********************************");
        String[] split = noLineBreak.split(",");
        //
        for(String subCompany : split){
            String operationSubCompany = subCompany.trim();
            execute_sql.append("-- ").append(operationSubCompany).append("\r\n");
            String new_sqlTemplate = sqlTemplate.replace("000000",operationSubCompany);
            execute_sql.append(new_sqlTemplate).append("\r\n");
        }
        return execute_sql.toString();
    }

    public void writerOut(String executeSql) {
        FileWriter fileWriter = null;//对原有文件的追加
        try {
            //1.提供File类的对象，指明写出到的文件
            File file = new File("D:\\io_test\\needExecuteSql.txt");
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