package com.bigDragon.javase.ioStream.pdf.itext7;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

/**
 * @author: bigDragon
 * @create: 2022/4/28
 * @Description:
 *      数据显示
 */
public class DataDisplay {
    public static void main(String[] args) {
        try {
            PdfWriter writer = new PdfWriter("D:\\documentOperation\\pdf\\DataDisplay.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(20, 20, 20, 20);
            //创建字体
            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            //创建Table对象，float类型为列宽
            Table table = new Table(new float[]{4, 4, 4, 4});
            //然后设置表格的相对于纸张的大小,100%
            table.setWidthPercent(100);
            //标题
            for(int i=0;i<4;i++){
                table.addHeaderCell(new Cell().add(new Paragraph("title"+i).setFont(bold)));
            }
            //模拟内容
            int num = 1;
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    table.addCell(new Cell().add(new Paragraph(""+num).setFont(font)));
                    num++;
                }
            }
            //测试换行
//            for(int i=0;i<4;i++){
//                for(int j=0;j<3;j++){
//                    table.addCell(new Cell().add(new Paragraph(""+num).setFont(bold)));
//                    num++;
//                }
//                table.startNewRow();
//            }

            document.add(table);
            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
