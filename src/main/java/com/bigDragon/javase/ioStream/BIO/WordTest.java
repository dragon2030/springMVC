package com.bigDragon.javase.ioStream.BIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author bigDragon
 * @create 2020-09-10 13:55
 */
public class WordTest {
    public static void main(String[] args){
        //参考博客 https://blog.csdn.net/a917606449/article/details/93617718

        WordTest wordTest = new WordTest();
        String Path5 = "src\\Main\\resources\\doc\\testDoc.docx";
        String Path6 = "src\\Main\\resources\\doc\\testDoc2.doc";
        String Path7 = "src\\Main\\resources\\doc\\testDoc3.docx";

        //word读取文件doc/docx
        //System.out.println(wordTest.readWord(Path6));
        //word写入文件docx
        wordTest.writeWord(Path7);
    }

    /**
     * word读取文件doc/docx
     * @param fileName
     * @return
     */
    public String readWord(String fileName){
        String text = "";
        try{
            FileInputStream is =  new FileInputStream(new File(fileName));
            POITextExtractor word = null;

            if (StringUtils.isBlank(fileName)) {
                return ("导入文档为空！");
            } else if (fileName.toLowerCase().endsWith("doc")) {
                word = new WordExtractor(is);
            } else if (fileName.toLowerCase().endsWith("docx")) {
                XWPFDocument doc = new XWPFDocument(is);
                word = new XWPFWordExtractor(doc);
            } else {
                return ("导入文档必须为word文档！");
            }
            text = word.getText();
        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return text;
    }

    /**
     * word写入文件docx
     * @param writePath
     */
    public void writeWord(String writePath){
        OutputStream  out = null;
        try {
            XWPFDocument createDoc = new XWPFDocument();
            XWPFParagraph ctPara = createDoc.createParagraph();
            // 一个XWPFRun代表具有相同属性的一个区域。
            XWPFRun ctRun = ctPara.createRun();
            ctPara.setIndentationFirstLine(567);// 首行缩进：567==1厘米 //
            // String ctText = para.getParagraphText();
            ctRun.setFontFamily("宋体");// 字体
            ctRun.setFontSize(12);
            ctRun.setTextPosition(6);//设置行间距
            String con = "tyuio";
            ctRun.setText(con);// 内容

            // 文件不存在时会自动创建
            out = new FileOutputStream(writePath);
            createDoc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
