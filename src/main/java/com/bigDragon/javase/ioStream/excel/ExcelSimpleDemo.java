package com.bigDragon.javase.ioStream.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 各类文件读取写入
 *
 * @author bigDragon
 * @create 2020-09-08 15:59
 */
public class ExcelSimpleDemo {
    public static void main(String[] args){
        ExcelSimpleDemo excelSimpleDemo = new ExcelSimpleDemo();
        String Path1 = "src\\Main\\resources\\excel\\testExcel.xls";
        String Path2 = "src\\Main\\resources\\excel\\testExcel2.xls";
        String Path3 = "src\\Main\\resources\\excel\\testExcel3.xlsx";
        String Path4 = "src\\Main\\resources\\excel\\testExcel4.xlsx";


       //excel操作 97-2003 xls(读取数据)
        System.out.println(excelSimpleDemo.readXls(Path1));
        //处理文件类型--Microsoft Excel 97-2003 工作表.xls （写入数据）
        excelSimpleDemo.writeXls(Path2);

        //excel操作  xlsx
        //处理文件类型--Microsoft Excel 工作表.xlsx (读取数据)
        System.out.println(excelSimpleDemo.readXlsx(Path3));
        //处理文件类型--Microsoft Excel 工作表.xlsx (写入数据)
        excelSimpleDemo.writeXlsx(Path4);

        //excel读取文件xls/xlsx
        System.out.println(excelSimpleDemo.readExcel(Path4));


    }

    /**
     * 处理文件类型--Microsoft Excel 97-2003 工作表.xls (读取数据)
     *
     * @param path
     * @return
     */
    public static String readXls(String path){
        String text="";
        try{
            FileInputStream is =  new FileInputStream(path);
            HSSFWorkbook excel=new HSSFWorkbook(is);
            //获取第一个sheet
            HSSFSheet sheet0=excel.getSheetAt(0);
            for (Iterator rowIterator = sheet0.iterator(); rowIterator.hasNext();){
                HSSFRow row=(HSSFRow) rowIterator.next();
                for (Iterator iterator=row.cellIterator();iterator.hasNext();){
                    HSSFCell cell=(HSSFCell) iterator.next();
                    //根据单元的的类型 读取相应的结果
                    if(cell.getCellType()== CellType.STRING) text+=cell.getStringCellValue()+"\t";
                    else if(cell.getCellType()==CellType.NUMERIC) text+=cell.getNumericCellValue()+"\t";
                    else if(cell.getCellType()==CellType.FORMULA) text+=cell.getCellFormula()+"\t";
                }
                text+="\n";
            }
        }catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 处理文件类型--Microsoft Excel 97-2003 工作表.xls （写入数据）
     *
     * @param path
     * @return
     */
    public void writeXls(String path) {
        FileOutputStream out = null;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();//创建Excel文件(Workbook)
            HSSFSheet sheet = workbook.createSheet("Test");//创建工作表(Sheet)
            HSSFRow row = sheet.createRow(0);// 创建行,从0开始

            HSSFCell  cell = row.createCell(0);
            cell.setCellValue("李志伟");// 创建行的单元格,也是从0开始
            row.createCell(1).setCellValue(false);// 设置单元格内容,重载
            row.createCell(2).setCellValue(new Date());// 设置单元格内容,重载
            row.createCell(3).setCellValue(12.345);// 设置单元格内容,重载

            HSSFCellStyle cellStyle = workbook.createCellStyle();//创建单元格样式
            Font font = workbook.createFont();//设置字体颜色
            font.setColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            File file=new File(path);
            file.createNewFile();
            out = new FileOutputStream(file);
            workbook.write(out);//保存Excel文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();//关闭文件流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理文件类型--Microsoft Excel 工作表.xlsx (读取数据)
     * @param path
     * @return
     */
    public static String readXlsx(String path){
        String text="";
        try{
            File file=new File(path);
            System.out.println(file.exists());
            FileInputStream is =  new FileInputStream(file);
            XSSFWorkbook excel=new XSSFWorkbook(is);
            //获取第一个sheet
            XSSFSheet sheet0=excel.getSheetAt(0);
            for (Iterator rowIterator=sheet0.iterator();rowIterator.hasNext();){
                XSSFRow row=(XSSFRow) rowIterator.next();
                for (Iterator iterator=row.cellIterator();iterator.hasNext();){
                    XSSFCell cell=(XSSFCell) iterator.next();
                    //根据单元的的类型 读取相应的结果
                    if(cell.getCellType()==CellType.STRING) text+=cell.getStringCellValue()+"\t";
                    else if(cell.getCellType()==CellType.NUMERIC) text+=cell.getNumericCellValue()+"\t";
                    else if(cell.getCellType()==CellType.FORMULA) text+=cell.getCellFormula()+"\t";
                }
                text+="\n";
            }
        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return text;
    }

    /**
     * 处理文件类型--Microsoft Excel 工作表.xlsx (写入数据)
     *
     * @param path
     */
    public void writeXlsx(String path) {
        FileOutputStream out = null;
        try {
            XSSFWorkbook workbook=new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Test");//创建工作表(Sheet)
            XSSFRow row = sheet.createRow(0);// 创建行,从0开始

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("李志伟");// 创建行的单元格,也是从0开始
            row.createCell(1).setCellValue(false);// 设置单元格内容,重载
            row.createCell(2).setCellValue(new Date());// 设置单元格内容,重载
            row.createCell(3).setCellValue(12.345);// 设置单元格内容,重载

            XSSFCellStyle cellStyle = workbook.createCellStyle();//创建单元格样式
            Font font = workbook.createFont();//设置字体颜色
            font.setColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            File file=new File(path);
            file.createNewFile();
            out = new FileOutputStream(file);
            workbook.write(out);//保存Excel文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();//关闭文件流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * excel读取文件xls/xlsx
     *
     * @param fileName
     * @return
     */
    public String readExcel(String fileName){
        String text = "";
        try{
            FileInputStream is =  new FileInputStream(new File(fileName));
            Workbook excel = null;

            if (StringUtils.isBlank(fileName)) {
                return ("导入文档为空！");
            } else if (fileName.toLowerCase().endsWith("xls")) {
                excel = new HSSFWorkbook(is);
            } else if (fileName.toLowerCase().endsWith("xlsx")) {
                excel = new XSSFWorkbook(is);
            } else {
                return ("导入文档必须为excel表格！");
            }
            //获取第一个sheet
            Sheet sheet0=  excel.getSheetAt(0);
            for (Iterator rowIterator=sheet0.iterator();rowIterator.hasNext();){
                Row row= (Row) rowIterator.next();
                for (Iterator iterator=row.cellIterator();iterator.hasNext();){
                    Cell cell=(Cell) iterator.next();
                    //根据单元的的类型 读取相应的结果
                    if(cell.getCellType()==CellType.STRING) text+=cell.getStringCellValue()+"\t";
                    else if(cell.getCellType()==CellType.NUMERIC) text+=cell.getNumericCellValue()+"\t";
                    else if(cell.getCellType()==CellType.FORMULA) text+=cell.getCellFormula()+"\t";
                }
                text+="\n";
            }
        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return text;
    }


    /**
     * excel数据处理
     *
     * @param fileName
     * @return
     */
    public List<String> excelDispose(String fileName){
        List<String> list = new ArrayList<String>();
        try{
            FileInputStream is =  new FileInputStream(new File(fileName));
            Workbook excel = null;

            if (StringUtils.isBlank(fileName)) {
                throw new RuntimeException("导入文档为空！");
            } else if (fileName.toLowerCase().endsWith("xls")) {
                excel = new HSSFWorkbook(is);
            } else if (fileName.toLowerCase().endsWith("xlsx")) {
                excel = new XSSFWorkbook(is);
            } else {
                throw new RuntimeException ("导入文档必须为excel表格！");
            }
            //获取第一个sheet
            Sheet sheet0=  excel.getSheetAt(0);
            for (Iterator rowIterator=sheet0.iterator();rowIterator.hasNext();){
                Row row= (Row) rowIterator.next();
                for (Iterator iterator=row.cellIterator();iterator.hasNext();){
                    Cell cell=(Cell) iterator.next();
                    String cellValue = "";//当前数值
                    //根据单元的的类型 读取相应的结果
                    switch (cell.getCellType()) {
                        case NUMERIC: //数字
                            //避免出现科学计数法
                            long longVal = Math.round(cell.getNumericCellValue());
                            Double doubleVal = cell.getNumericCellValue();
                            if (Double.parseDouble(longVal + ".0") == doubleVal){
                                cell.setCellType(CellType.STRING);
                                cellValue = cell.getStringCellValue();
                            }
                            break;
                        case STRING: //字符串
                            cellValue = String.valueOf(cell.getStringCellValue());
                            break;
                        case BOOLEAN: //Boolean
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA: //公式
                            cellValue = String.valueOf(cell.getCellFormula());
                            break;
                        case BLANK: //空值
                            cellValue = "";
                            break;
                        case ERROR: //故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                    list.add(cellValue);
                }
            }
        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }
}
