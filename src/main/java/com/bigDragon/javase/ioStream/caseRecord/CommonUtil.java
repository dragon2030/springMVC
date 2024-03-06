package com.bigDragon.javase.ioStream.caseRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CommonUtil {
    public static String readerTemplate(String pathName) {
        FileReader fr = null;
        StringBuffer sqlTemplate = new StringBuffer();
        try {
            File file = new File(pathName);
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

    public static void writerOut(String executeSql,String pathName) {
        CommonUtil.writerOut(executeSql,pathName,false);//默认不追加写入
    }
    public static void writerOut(String executeSql,String pathName,Boolean isAppend) {
        FileWriter fileWriter = null;//对原有文件的追加
        try {
            //1.提供File类的对象，指明写出到的文件
            File file = new File(pathName);
            if (!isAppend && file.exists()){
                file.delete();
                file.createNewFile();
            }
            fileWriter = new FileWriter(file,isAppend);
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
