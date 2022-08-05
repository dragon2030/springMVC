package com.bigDragon.documentOperation.poi.excel.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.bigDragon.documentOperation.poi.excel.annotation.Excel;
import com.bigDragon.documentOperation.poi.excel.enums.CellStyleEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: bigDragon
 * @create: 2022/1/13
 * @Description:
 *      Excel工具类
 */
public class ExcelUtil {
    /**
     * Excel导出
     * @param sheetName Sheet名字
     * @param pojoClass Excel对象Class类
     * @param dateCollection Excel对象数据Collection
     * @param outputStream 输出流
     * @param excludeColumn 需要动态排除的列
     * @param workbook 设置表头，主要针对于需要合并同类项的多行表头
     * @throws Exception
     */
    //TODO 这边两个泛型是相同的，可以用泛型方法统一两个泛型的传入
    public static void exportExcel (String sheetName, Class<?> pojoClass, List<?> dateCollection,
                                    OutputStream outputStream, String[] excludeColumn, Workbook workbook) throws Exception{
        //使用userModel模式实现的，当excel文档出现10万级别的大数据文件时可能导致oom内存溢出
        exportExcelInUserModel(sheetName, pojoClass, dateCollection, outputStream, excludeColumn, workbook);
        //使用eventModel模式实现，可以一边读一边处理，效率较高，但实现复杂
    }


    /**
     * Excel导出-使用userModel模式实现
     *
     * 核心思想：
     *      创建三个ArrayList，数量对应导出实体类中@Excel的数量
     *      uniqueFieldName唯一字段名列表
     *      exportFieldTitle存各列标题标题
     *      exportFieldWidth存列名宽度
     *      methodObject存导出字段在实体类中的get方法——用于在遍历导出Collection集合中，取得字段值
     *      mergeSiteArray一个二维数组，记录此excel合并单元格位置信息
     *      excludeColumn需要动态排除的列，根据传入参数，动态将某添加@Excel的列排除不显示
     *      wk 设置表头，主要针对于需要合并同类项的多行表头
     *  写的我头发都掉光了，先这样吧，以后再优化修改，等看完poi的网课再刷一下官网api和大神代码。
     */
    public static void exportExcelInUserModel(String sheetName, Class<?> pojoClass, List<?> dateList,
                                              OutputStream outputStream, String[] excludeColumn, Workbook wk) throws Exception{
        //数据非空验证
        if(sheetName == null || outputStream == null || pojoClass == null){
            throw new RuntimeException("传入参数不可为空！");
        }

        //变量初始化
        //声明一个工作簿
        //TODO 需要改造为xlsx格式
        Workbook workbook = new XSSFWorkbook();
        //声明一个表格
        Sheet sheet = workbook.createSheet(sheetName);
        // 唯一字段名列表：主要用于字段上的特殊限制，如合并标识，后续存入此唯一值在set/map数据插入时开启判断
        List<String> uniqueFieldName = new ArrayList<>();
        // 列名标题
        List<String> exportFieldTitle = new ArrayList<>();
        // 列名宽度
        List<Integer> exportFieldWidth = new ArrayList<>();
        // 用于拿到dataCollection中字段值，对应导出字段在实体类中的get方法
        List<Method> methodObject = new ArrayList<>();
        // 标注mergeCellSign的方法，存唯一字段名（便于快速找到）
        Set<String> mergeCellSet = new HashSet<>();
        //初始化一个二维数组，记录此excel合并单元格位置信息，与导入date位置一一对应，行为导出excel字段长度，列为导出数据最大长度,当值为1时，此位置对应的date数据需要合并单元格
        //解决三个问题，1、合并位置记录问题2、不改变原本date数据3、不同合并单元格相同列内容可以区分（uuid）
        String[][] mergeSiteArray = null;
        //excludeColumn转换为set
        Set<String> excludeColumnSet = excludeColumn!=null?Arrays.stream(excludeColumn).collect(Collectors.toSet()):new HashSet<>();
        //自定义数据格式map，key为唯一字段名列表uniqueFieldName
        Map<String,String> selfFormatMap = new HashMap<>();

        //得到目标类的所有字段列表
        Field[] fields = pojoClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            Excel excel = field.getAnnotation(Excel.class);
            if(excel != null){
                //判断excludeColumn排除列
                if(excludeColumnSet.contains(field.getName())){
                    continue;
                }
                //添加到唯一字段名列表
                uniqueFieldName.add(field.getName());
                //添加到标题
                exportFieldTitle.add(excel.exportName());
                //添加到标题的列宽
                exportFieldWidth.add(excel.exportFieldWidth());
                //添加到导出字段的get方法
                String fieldName = field.getName();
                StringBuilder getMethodName = new StringBuilder("get")
                        .append(fieldName.substring(0,1).toUpperCase())
                        .append(fieldName.substring(1));
                Method getMethod = pojoClass.getDeclaredMethod(getMethodName.toString());
                methodObject.add(getMethod);
                if(excel.mergeCellSign() == 1){
                    mergeCellSet.add(field.getName());
                }
                if(StringUtils.isNotBlank(excel.selfFormat())){
                    selfFormatMap.put(field.getName(), excel.selfFormat());
                }
            }
        }

        //实体类中未找到标注Excel注解的字段，报错,不执行后续代码
        if(CollectionUtil.isEmpty(uniqueFieldName)){
            throw new RuntimeException("实体类中未找到标注Excel注解的字段!");
        }
        //二维数组动态初始化
        mergeSiteArray = new String[dateList.size()][uniqueFieldName.size()];
        //设置样式
        CellStyle[] cellStyleArray = createCellStyleArray(workbook,wk);

        //行指针，根据此指针createRow
        int index = 0;
        Row row = null;
        //标题行titleRows，不设置特殊标题时，标题为一行
        Integer titleRows = 1;

        if(wk == null){
            //产生标题行
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            row = sheet.createRow(index);
            for(int i = 0; i < exportFieldTitle.size(); i++){
                Cell cell = row.createCell(i);
                RichTextString text = new XSSFRichTextString((exportFieldTitle.get(i)));
                text.applyFont(titleFont);
                cell.setCellValue(text);
                cell.setCellStyle(cellStyleArray[0]);
            }
        } else {
            //替换的标题行
            workbook = wk;
            sheet = workbook.getSheetAt(0);
            index = sheet.getLastRowNum();
            titleRows = sheet.getLastRowNum()+1;
        }

        //设置标题行列宽
        for(int i = 0; i < exportFieldWidth.size(); i++){
            //256=65280/255
            sheet.setColumnWidth(i,256 * exportFieldWidth.get(i));
        }

        //迭代遍历集合，写入数据
        for(int i = 0; i < dateList.size(); i++){//行
            //从第二行开始写，第一行是标题
            index++;
            row = sheet.createRow(index);
            Object dataObject = dateList.get(i);
            for(int j = 0; j < uniqueFieldName.size(); j++){//列 循环一次写入一条字段数据
                Cell cell = row.createCell(j);
                Method getMethod = methodObject.get(j);
                Object value = getMethod.invoke(dataObject);
//                String strValue = value == null ? "" : value.toString();
//                cell.setCellValue(strValue);
                String selfFormat = selfFormatMap.get(uniqueFieldName.get(j));
//                if(wk == null){
                setCellValueByObject(cell, value, cellStyleArray, selfFormat);
//                } else {
//                    setCellValueByOtherWorkbook(cell, value, cellStyleArray, selfFormat);
//                }

                //合并单元格判断:当前列是否属于标记合并的列 && 第一行或在之前行判断中此cell不为合并单元格 && 【判断下一行】不是最后一行且和下一个行的值相同
                if(mergeCellSet.contains(uniqueFieldName.get(j)) &&
                        (i == 0 || mergeSiteArray[i][j] == null) &&
                        (i != dateList.size()-1 && Objects.equals(value, getMethod.invoke(dateList.get(i+1))))
                ){
                    //调用合并方法：计算当前列需要合并数，再重复计算其父（超）列合并数，得到需要合并量进行合并单元格操作
                    //循环获取下一个值，直到下个值不同或到达最后一行，取相同的行数
                    int mergeNum;
                    int currentMergeNum = 1;
                    //当前列从该行开始有多少重复
                    for(int k = i; k < dateList.size();k++){//行，判断有下一行且下一行相等，当前合并数加1
                        if (k < dateList.size()-1 && Objects.equals(value, getMethod.invoke(dateList.get(k+1)))) {
                            currentMergeNum++;
                        } else {
                            break;
                        }
                    }
                    mergeNum = currentMergeNum;
                    //判断父合并列，当前列非第一列且前一列是需合并列，为有父合并列，需要判断其父列的合并数,再判断其超列,取最小合并数字为需要合并的单元格数
                    //循环匹配每一个上一行
                    int thisColumn = j;//当前合并列
                    if(thisColumn != 0 && mergeCellSet.contains(uniqueFieldName.get(thisColumn-1))){//为有父合并列
                        //父合并列索引赋值给当前合并列
                        thisColumn = thisColumn-1;

                        //当前列从该行开始有多少合并单元格
                        //如果父行没有合并单元格，直接不开启合并单元格
                        if(mergeSiteArray[i][thisColumn]==null){
                            mergeNum = 1;
                        }
                        int parentMergeNum = 0;
                        //如果父行有合并单元格,判断其合并数量
                        for(int k = i; k < dateList.size();k++){
                            if(Objects.equals(mergeSiteArray[i][thisColumn], mergeSiteArray[k][thisColumn])){
                                parentMergeNum++;
                            } else {
                                break;
                            }
                        }

                        //如果当前合并列合并数小于子合并列合并数，取当前合并数
                        if(mergeNum > parentMergeNum){
                            mergeNum = parentMergeNum;
                        }
//                        if(mergeNum == 1){
//                            break;
//                        }
                    }
                    //合并单元格，mergeNum为合并单元格数，mergeNum-1为需要向下行合并行的数量,最后加1,因为第一行为标题行
                    //标题行titleRows，不设置特殊标题时，标题为一行
                    if(mergeNum > 1){
                        CellRangeAddress region = new CellRangeAddress(i+titleRows, i+mergeNum-1+titleRows, j, j);
                        sheet.addMergedRegion(region);
                        UUID uuid = UUID.randomUUID();
                        for(int site = i; site < i+mergeNum; site++){
                            mergeSiteArray[site][j] = uuid.toString();
                        }
                    }
                }
            }
        }
        workbook.write(outputStream);
        //todo 测试
//        String format = new SimpleDateFormat("yyyyMMddHHmmSS").format(new Date());
//        File file=new File("C:\\Users\\bigDragon\\Desktop\\work\\先锋\\报表\\"+format+".xls");
//        file.createNewFile();
//        OutputStream out2 = new FileOutputStream(file);
//        workbook.write(out2);
    }

    //获取cell样式数组，0索引为string可惜，1索引为日期类型,2索引为整型，3索引为浮点型
    public static CellStyle[] createCellStyleArray(Workbook workbook, Workbook importWorkbook){
        if(importWorkbook!=null){
            workbook=importWorkbook;
        }
        List<CellStyle> cellStyles = new ArrayList<>();

        //字符串
        CellStyle style_str = workbook.createCellStyle();
        style_str.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        style_str.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyles.add(CellStyleEnum.STRING_CELL_STYLE.getCode(),style_str);

        //日期
        CellStyle style_date = workbook.createCellStyle();
        DataFormat dataFormat= workbook.createDataFormat();
        style_date.setDataFormat(dataFormat.getFormat("yyyy-mm-dd h:mm:ss"));
        style_date.setAlignment(HorizontalAlignment.CENTER);
        style_date.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyles.add(CellStyleEnum.DATE_CELL_STYLE.getCode(),style_date);

        //整型
        CellStyle intStyle = workbook.createCellStyle();
        DataFormat intFormat= workbook.createDataFormat();
        intStyle.setDataFormat(intFormat.getFormat("0"));
        intStyle.setAlignment(HorizontalAlignment.CENTER);
        intStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyles.add(CellStyleEnum.INTEGER_CELL_STYLE.getCode(),intStyle);

        //浮点型
        CellStyle doubleStyle = workbook.createCellStyle();
        DataFormat doubleFormat= workbook.createDataFormat();
        doubleStyle.setDataFormat(doubleFormat.getFormat("0.00"));
        doubleStyle.setAlignment(HorizontalAlignment.CENTER);
        doubleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyles.add(CellStyleEnum.DOUBLE_CELL_STYLE.getCode(),doubleStyle);

        //日期2
        CellStyle style_date2 = workbook.createCellStyle();
        DataFormat dataFormat2= workbook.createDataFormat();
        style_date2.setDataFormat(dataFormat2.getFormat("yyyy-mm-dd"));
        style_date2.setAlignment(HorizontalAlignment.CENTER);
        style_date2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyles.add(CellStyleEnum.DATE_CELL_STYLE2.getCode(),style_date2);

        return cellStyles.toArray(new CellStyle[cellStyles.size()]);
    }


    /**
     * 设置单元格值
     * 根据值类型，设置单元格值
     * @param cell 单元格对象
     * @param value 输入值
     * @param cellStyleArray cellStyle数值，预赋值，不用重复创建
     * @param selfFormat 特殊文本格式，没用则为null
     */
    public static void setCellValueByObject(Cell cell, Object value, CellStyle[] cellStyleArray,
                                            String selfFormat) {
        if (value == null){
            cell.setCellValue("");
            return;
        }
        if (value instanceof String){//字符串
            String strValue = value.toString();
            cell.setCellValue(strValue);
            cell.setCellStyle(cellStyleArray[CellStyleEnum.STRING_CELL_STYLE.getCode()]);
        } else if (value instanceof Date){//日期
            Date dateValue = (Date)value;
            cell.setCellValue(dateValue);
            if(Objects.equals(selfFormat,"yyyy-mm-dd")){
                cell.setCellStyle(cellStyleArray[CellStyleEnum.DATE_CELL_STYLE2.getCode()]);
            } else{//自定义规则
                cell.setCellStyle(cellStyleArray[CellStyleEnum.DATE_CELL_STYLE.getCode()]);
            }
        } else if (value instanceof Integer){//整型
            Integer integer = (Integer)value;
            cell.setCellValue(integer);
            cell.setCellStyle(cellStyleArray[CellStyleEnum.INTEGER_CELL_STYLE.getCode()]);
        } else if (value instanceof BigDecimal){//浮点型
            BigDecimal bigDecimal = (BigDecimal)value;
            double doubleValue = bigDecimal.doubleValue();
            cell.setCellValue(doubleValue);
            cell.setCellStyle(cellStyleArray[CellStyleEnum.DOUBLE_CELL_STYLE.getCode()]);
        } else {
            throw new RuntimeException("此单元格数据类型无法解析，请联系管理员: " + value.toString());
        }
    }

    /**
     * 别的Workbook设置style
     * 设置单元格值
     * 根据值类型，设置单元格值
     */
    public static void setCellValueByOtherWorkbook(Cell cell, Object value, CellStyle[] cellStyleArray,
                                                   String selfFormat) {
        if (value == null){
            cell.setCellValue("");
            return;
        }
        if (value instanceof String){//字符串
            String strValue = value.toString();
            cell.setCellValue(strValue);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.cloneStyleFrom(cellStyleArray[0]);
            cell.setCellStyle(cellStyle);
        } else if (value instanceof Date){//日期
            Date dateValue = (Date)value;
            cell.setCellValue(dateValue);
            if(Objects.equals(selfFormat,"yyyy-mm-dd")){
                cell.getCellStyle().cloneStyleFrom(cellStyleArray[CellStyleEnum.DATE_CELL_STYLE2.getCode()]);
            } else{//自定义规则
                cell.getCellStyle().cloneStyleFrom(cellStyleArray[CellStyleEnum.DATE_CELL_STYLE.getCode()]);
            }
        } else if (value instanceof Integer){//整型
            Integer integer = (Integer)value;
            cell.setCellValue(integer);
            cell.getCellStyle().cloneStyleFrom(cellStyleArray[2]);
        } else if (value instanceof BigDecimal){//浮点型
            BigDecimal bigDecimal = (BigDecimal)value;
            double doubleValue = bigDecimal.doubleValue();
            cell.setCellValue(doubleValue);
            cell.getCellStyle().cloneStyleFrom(cellStyleArray[3]);
        } else {
            throw new RuntimeException("此单元格数据类型无法解析，请联系管理员: " + value.toString());
        }
    }

    /**
     * 空数据时，返回完整表头的excel数据
     * 于exportExcelInUserModel方法中提取
     * @param sheetName Sheet名字
     * @param pojoClass Excel对象Class类
     * @param outputStream 输出流
     * @param excludeColumn 需要动态排除的列
     * @param wk 设置表头，主要针对于需要合并同类项的多行表头
     */
    public static void exportExcelTitle(String sheetName, Class<?> pojoClass,
                                        OutputStream outputStream, String[] excludeColumn, Workbook wk) throws Exception{
        if(sheetName == null || outputStream == null || pojoClass == null){
            throw new RuntimeException("传入参数不可为空！");
        }
        //变量初始化
        //声明一个工作簿
        Workbook workbook = new XSSFWorkbook();
        //声明一个表格
        Sheet sheet = workbook.createSheet(sheetName);
        // 唯一字段名列表：主要用于字段上的特殊限制，如合并标识，后续存入此唯一值在set/map数据插入时开启判断
        ArrayList<String> uniqueFieldName = new ArrayList<>();
        // 列名标题
        ArrayList<String> exportFieldTitle = new ArrayList<>();
        // 列名宽度
        ArrayList<Integer> exportFieldWidth = new ArrayList<>();
        //excludeColumn转换为set
        Set<String> excludeColumnSet = excludeColumn!=null?Arrays.stream(excludeColumn).collect(Collectors.toSet()):new HashSet<>();

        //得到目标类的所有字段列表
        Field[] fields = pojoClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            Excel excel = field.getAnnotation(Excel.class);
            if(excel != null){
                //判断excludeColumn排除列
                if(excludeColumnSet.contains(field.getName())){
                    continue;
                }
                //添加到唯一字段名列表
                uniqueFieldName.add(field.getName());
                //添加到标题
                exportFieldTitle.add(excel.exportName());
                //添加到标题的列宽
                exportFieldWidth.add(excel.exportFieldWidth());
            }
        }

        //实体类中未找到标注Excel注解的字段，报错,不执行后续代码
        if(CollectionUtil.isEmpty(uniqueFieldName)){
            throw new RuntimeException("实体类中未找到标注Excel注解的字段!");
        }
        //设置样式
        CellStyle[] cellStyleArray = createCellStyleArray(workbook,wk);

        //行指针，根据此指针createRow
        int index = 0;

        if(wk == null){
            //产生标题行
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            Row row = sheet.createRow(index);
            for(int i = 0; i < exportFieldTitle.size(); i++){
                Cell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(exportFieldTitle.get(i));
                text.applyFont(titleFont);
                cell.setCellValue(text);
                cell.setCellStyle(cellStyleArray[0]);
            }
        } else {
            //替换的标题行
            workbook = wk;
            sheet = workbook.getSheetAt(0);
        }

        //设置标题行列宽
        for(int i = 0; i < exportFieldWidth.size(); i++){
            //256=65280/255
            sheet.setColumnWidth(i,256 * exportFieldWidth.get(i));
        }
        workbook.write(outputStream);
    }



    /**
     * Excel导入
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
