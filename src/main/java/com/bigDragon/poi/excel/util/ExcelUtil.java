package com.bigDragon.poi.excel.util;

import com.bigDragon.javase.faseToObject.interfasce.C;
import com.bigDragon.poi.excel.Excel;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.chrono.MinguoDate;
import java.util.*;

/**
 * @author: bigDragon
 * @create: 2022/1/13
 * @Description:
 *      Excel工具类
 */
public class ExcelUtil {
    /**
     * Excel导出
     * @param title Sheet名字
     * @param pojoClass Excel对象Class类
     * @param dateCollection Excel对象数据Collection
     * @param outputStream 输出流
     * @throws Exception
     */
    //TODO 这边两个泛型是相同的，可以用泛型方法统一两个泛型的传入
    public static void exportExcel (String title, Class<?> pojoClass, Collection<?> dateCollection, OutputStream outputStream) throws Exception{
        //使用userModel模式实现的，当excel文档出现10万级别的大数据文件时可能导致oom内存溢出
        exportExcelInUserModel(title,pojoClass,dateCollection,outputStream);
        //使用eventModel模式实现，可以一边读一边处理，效率较高，但实现复杂
    }

    //Excel导出-使用userModel模式实现
    public static void exportExcelInUserModel(String title, Class<?> pojoClass,
                                              Collection<?> dateCollection, OutputStream outputStream) throws Exception{
        //数据非空验证
        if(dateCollection == null || dateCollection.size() == 0){
            throw new RuntimeException("导出数据为空！");
        }
        if(title == null || outputStream == null || pojoClass == null){
            throw new RuntimeException("传入参数不可为空！");
        }

        //变量初始化
        //声明一个工作簿
        Workbook workbook = new HSSFWorkbook();
        //声明一个表格
        Sheet sheet = workbook.createSheet(title);
        // 列名标题
        ArrayList<String> exportFieldTitle = new ArrayList<>();
        // 列名宽度
        ArrayList<Integer> exportFieldWidth = new ArrayList<>();
        // 用于拿到所有列名，对应导出字段在实体类中的get方法
        ArrayList<Method> methodObject = new ArrayList<>();
        // 标注exportConvertSign的方法，key存getMethod方法名，value存getMethodConvert的Method类
        // （因为在导出列时会按照上面的methodObject遍历，此设计可以便于快速找到methodObject中某个值）
        Map<String,Method> covertMethodMap = new HashMap<>();

        //得到目标类的所有字段列表
        Field[] fields = pojoClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            Excel excel = field.getAnnotation(Excel.class);
            //TODO 这边写的应该有问题，为空时应该return或者异常，产生空文件或者提示文件为空，不应该执行多余代码
            if(excel != null){
                //添加到标题
                exportFieldTitle.add(excel.exportName());
                //添加到标题的列宽
                exportFieldWidth.add(excel.exportFieldWidth());
                //添加到导出字段的get方法
                String fieldName = field.getName();
                StringBuilder getMethodName = new StringBuilder("get")
                        .append(fieldName.substring(0,1).toUpperCase())
                        .append(fieldName.substring(1));
                //TODO 有必要写new Class[]{}?
                Method getMethod = pojoClass.getDeclaredMethod(getMethodName.toString(),new Class[]{});
                methodObject.add(getMethod);
                if(excel.exportConvertSign() == 1){
                    StringBuilder getConvertMethodName = new StringBuilder("get")
                            .append(fieldName.substring(0, 1).toUpperCase())
                            .append(fieldName.substring(1))
                            .append("Convert");
                    Method getConvertMethod = pojoClass.getMethod(getConvertMethodName.toString(), new Class[]{});
                    covertMethodMap.put(getMethodName.toString(),getConvertMethod);
                }
            }
        }

        //行指针
        int index = 0;

        //产生标题行
        Row row = sheet.createRow(index);
        for(int i = 0; i < exportFieldTitle.size(); i++){
            Cell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(exportFieldTitle.get(i));
            cell.setCellValue(text);
        }
        //设置标题行列宽
        for(int i = 0; i < exportFieldWidth.size(); i++){
            //256=65280/255
            sheet.setColumnWidth(i,256 * exportFieldWidth.get(i));
        }

        //迭代遍历集合，写入数据
        Iterator iterator = dateCollection.iterator();
        if(iterator.hasNext()){//循环一次设置一行数据
            //从第二行开始写，第一行是标题
            index++;
            row = sheet.createRow(index);
            Object dataObject = iterator.next();
            for(int i = 0; i < methodObject.size(); i++){//循环一次设置一列数据
                Cell cell = row.createCell(i);
                Method getMethod = methodObject.get(i);
                Object value = null;
                if(covertMethodMap.containsKey(getMethod.getName())){//如果key值匹配上，就走转换方法不走直接get的普通方法
                    Method method = covertMethodMap.get(getMethod.getName());
                    value = method.invoke(dataObject, new Object[]{});
                } else {
                    value = getMethod.invoke(dataObject, new Object() {});
                }
                cell.setCellValue(value == null ? "" : value.toString());
            }
        }
        workbook.write(outputStream);
    }

    /**
     * Excel导出
     * @param file
     * @param pojoClass
     * @return
     */
    public static List importExcel(File file, Class pojoClass) throws Exception{
        //使用userModel模式实现的
        return importExcelInUserModel(file,pojoClass);
    }

    public static List importExcelInUserModel(File file, Class pojoClass) throws Exception{
        //输出的list
        List<Object> dateList = new ArrayList<>();

        //所有标有Annotation的字段，也就是允许导入的字段，放入一个map中,key值为exportName，value值为Method
        Map<String, Method> exportNameMethodMap = new HashMap<>();
        //所有标有Annotation的字段中importConvertSign值为1的字段，放到一个map中，key值为exportName，value值为ConvertMethod
        Map<String, Method> exportNameMethodConvertMap = new HashMap<>();

        //得到目标类的所有字段列表
        Field[] fields = pojoClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            Excel excel = field.getAnnotation(Excel.class);
            if(excel != null){
                //标注了Annotation字段的Setter方法
                String fieldName = field.getName();
                StringBuilder setMethodName = new StringBuilder("set")
                        .append(fieldName.substring(0, 1).toUpperCase())
                        .append(fieldName.substring(1));
                //构造调用的Method
                Method setMethod = pojoClass.getDeclaredMethod(setMethodName.toString(), new Class[]{field.getType()});
                //将这个method以Annotation中exportName字段为key set方法为value
                //TODO（exportName重名将导致异常）
                exportNameMethodMap.put(excel.exportName(), setMethod);
                if(excel.importConvertSign() == 1){
                    StringBuilder setConvertMethodName = new StringBuilder("set")
                            .append(fieldName.substring(0, 1).toUpperCase())
                            .append(fieldName.substring(1))
                            .append("Convert");
                    Method setConvertMethod = pojoClass.getMethod(setConvertMethodName.toString(), new Class[]{});
                    exportNameMethodConvertMap.put(excel.exportName(),setConvertMethod);

                }
            }
        }

        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(inputStream);
        int index = 0;
        //得到第一页的sheet
        Sheet sheet = workbook.getSheetAt(index);
        //得到第一面的所有行
        Iterator<Row> rowIterator = sheet.rowIterator();
        //得到第一行数据，就是标题行
        Row titleRow = rowIterator.next();
        //得到第一行所有列
        Iterator<Cell> titleCellIterator = titleRow.cellIterator();
        //存放标题内容的List
        List<String> titleNameList = new ArrayList<>();
        while (titleCellIterator.hasNext()){
            Cell cell = titleCellIterator.next();
            titleNameList.add(cell == null ? "" : cell.getStringCellValue());
        }

        //遍历除标题列外剩余行，进行数据装配
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            //得到传入类的实例
            Object dataObject = pojoClass.newInstance();
            //遍历一行中的列
            for(int i = 0; i < titleNameList.size(); i++){
                Cell cell = row.getCell(i);
                //标题内容
                String titleString = titleNameList.get(i);
                //如果此一列的标题和pojoClass类中的某一列的exportName属性相同，则调用此类的set方法，进行设值
                if(exportNameMethodMap.containsKey(titleString)){
                    Method setMethod = exportNameMethodMap.get(titleString);
                    //获取到setMethod第一个参数的类型
                    //TODO 写的会有问题 应该直接比较与class
                    String setMethodParam = setMethod.getGenericParameterTypes()[0].toString();
                    //TODO 这一块格式解析的代码应该是可以优化的
                    //判断参数类型
                    if(exportNameMethodConvertMap.containsKey(titleString)){//字段有设置转义就走转义的set方法
                        Method method = exportNameMethodConvertMap.get(titleString);
                        method.invoke(dataObject,cell.getStringCellValue());
                    }else{
                        if("class java.lang.String".equals(setMethodParam)){
                            String strCell = "";
                            if(cell != null){
                                switch (cell.getCellType()){
                                    case STRING:
                                        strCell = cell.getStringCellValue();
                                        break;
                                    case NUMERIC:
                                        strCell = String.valueOf(cell.getNumericCellValue());
                                        break;
                                    case BOOLEAN:
                                        strCell = String.valueOf(cell.getBooleanCellValue());
                                        break;
                                    default:
                                        strCell = "";
                                        break;
                                }
                                if(null == strCell){
                                    strCell = "";
                                }
                                setMethod.invoke(dataObject,strCell);
                            }
                        } else if("class java.lang.Boolean".equals(setMethodParam)){
                            setMethod.invoke(dataObject,cell.getBooleanCellValue());
                        } else if("class java.lang.Integer".equals(setMethodParam)){
                            setMethod.invoke(dataObject, new Integer(cell.getStringCellValue()));
                        } else if("class java.lang.Long".equals(setMethodParam)){
                            setMethod.invoke(dataObject, new Long(cell.getStringCellValue()));
                        }
                    }
                }
            }
        }

        return dateList;
    }

}
