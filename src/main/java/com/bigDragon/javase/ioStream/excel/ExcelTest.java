package com.bigDragon.javase.ioStream.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @author: bigDragon
 * @create: 2022/11/30
 * @Description:
 */
public class ExcelTest {
    public static void main(String[] args) {
        ExcelTest excelTest = new ExcelTest();
        String Path = "D:\\documentOperation\\excel\\testExcel.xls";

        excelTest.writeXlsx(Path);
    }

    public void writeXlsx(String path) {
        FileOutputStream out = null;
        try {
            Workbook workbook=new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Test");//创建工作表(Sheet)
            Row row = sheet.createRow(0);// 创建行,从0开始

            Cell cell = row.createCell(0);
            cell.setCellValue("李志伟");// 创建行的单元格,也是从0开始
            row.createCell(1).setCellValue(false);// 设置单元格内容,重载
            row.createCell(2).setCellValue(new Date());// 设置单元格内容,重载
            row.createCell(3).setCellValue(12.345);// 设置单元格内容,重载

            CellStyle cellStyle = workbook.createCellStyle();//创建单元格样式
            Font font = workbook.createFont();//设置字体颜色
            font.setColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            File file=new File(path);
            file.createNewFile();
            out = new FileOutputStream(file);
            //下拉提示
            excelSelectInit(sheet,1,1,new String[]{"1","2"});
            workbook.write(out);//保存Excel文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.close();//关闭文件流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void excelSelectInit(Sheet sheet, int firstCol, int lastCol, String[] strings) {
        //  生成下拉列表
        //  只对(x，x)单元格有效
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 65535, firstCol, lastCol);
        //  生成下拉框内容
        DataValidationHelper help = new XSSFDataValidationHelper((XSSFSheet) sheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) help
                .createExplicitListConstraint(strings);
        XSSFDataValidation dataValidation = (XSSFDataValidation) help.createValidation(dvConstraint, cellRangeAddressList);
        //  对sheet页生效
        sheet.addValidationData(dataValidation);
    }

}
