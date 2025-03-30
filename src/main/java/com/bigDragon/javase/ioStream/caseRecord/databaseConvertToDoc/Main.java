package com.bigDragon.javase.ioStream.caseRecord.databaseConvertToDoc;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

/**
 * @author yinsheng
 * @date 2024/11/13
 * 先锋-尹盛 写用于按照数据库元数据生成doc产品文档
 */
public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.5.150:8035/mr_innover?nullDatabaseMeansCurrent=true&characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai",
                "xfuser1","Ihave0123&UPPERCASE");
        String queryTable = "SELECT " +
                "table_name as table_name , if(table_comment is null or table_comment = '','--',table_comment) as table_comment " +
                "FROM " +
                "    INFORMATION_SCHEMA.TABLES " +
                "WHERE " +
                "    TABLE_SCHEMA = 'mr_innover' ";
        Map<String,String> tables  = new LinkedHashMap<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryTable);

            while (rs.next()) {
                // 假设有一个名为"column_name"的列
                String data = rs.getString("table_name");
                String table_comment = rs.getString("table_comment");
                tables.put(data,table_comment);
            }
        }

        String sql = "select \n" +
                "CAST((@row_number := @row_number + 1) AS CHAR) AS `序号`,\n"+
                "ifnull(`Field`,'-') as '字段名',\n" +
                "    ifnull(`Type`,'-') as '类型',\n" +
                "   ifnull( `Length`,'-') as '长度',\n" +
                "     if(`NotNull`,'NO','YES')as '是否为空',\n" +
                "     ifnull(`Default`,'-')as '默认值',\n" +
                "    ifnull( `Scale`,'-')as '小数位',\n" +
                "     ifnull(`Comment`,'-')as '注释'\n" +
                "\n" +
                "\n" +
                "from (\n" +
                "SELECT\n" +
                "\t\n" +
                "    COLUMN_NAME AS `Field`,\n" +
                "    COLUMN_TYPE AS `Type`,\n" +
                "    CHARACTER_MAXIMUM_LENGTH AS `Length`,\n" +
                "    COLUMN_NAME IN (SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '{}' AND IS_NULLABLE = 'NO') AS `NotNull`,\n" +
                "    COLUMN_DEFAULT AS `Default`,\n" +
                "    NUMERIC_SCALE AS `Scale`,\n" +
                "    COLUMN_COMMENT AS `Comment`\n" +
                "FROM\n" +
                "    INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE\n" +
                "    TABLE_SCHEMA = 'mr_innover' \n" +
                "    AND TABLE_NAME = '{}'\n" +
                ") as temp  ";


        Map<String,List<Map<String,String>>> map = new LinkedHashMap<>();
        for (String table : tables.keySet()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET @row_number = 0;");
                ResultSet rs = stmt.executeQuery(StrUtil.format(sql,table,table));
                List<Map<String,String>> item = new ArrayList<>();
                while (rs.next()) {
                    Map<String,String> itemItem = new HashMap<>();

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();


                    for (int i = 1; i <= columnCount; i++) {
                        itemItem.put(metaData.getColumnName(i), (String)rs.getObject(i));
                    }
                    item.add(itemItem);
                }
                map.put(table,item);
            }
        }
        System.out.println(map);

        exportDoc(map,tables);

    }

    static void exportDoc( Map<String,List<Map<String,String>>> map,Map<String,String> tables) throws Exception {
        XWPFDocument document = new XWPFDocument();

        // 添加一级标题
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun run1 = paragraph1.createRun();
        run1.setText("数据库");
        run1.setBold(true);
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        paragraph1.setStyle("标题 2");

        int i = 1;
        for (Map.Entry<String,String> entry : tables.entrySet()) {
            String tableName = entry.getKey();
            String content = entry.getValue();
            List<Map<String,String>> fieldMap = map.get(tableName);
            // 添加一级标题
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(StrUtil.concat(true,String.valueOf(i++),"、",tableName,"[",content,"]"));
            run.setBold(true);
            paragraph.setAlignment(ParagraphAlignment.LEFT);
            paragraph.setStyle("标题 3");
            int j = 1;

            // 创建表格
            XWPFTable table = document.createTable(fieldMap.size()+1, 8);
            buildCell(0,0,table,"序号");
            buildCell(0,1,table,"字段名");
            buildCell(0,2,table,"类型");
            buildCell(0,3,table,"长度");
            buildCell(0,4,table,"是否为空");
            buildCell(0,5,table,"默认值");
            buildCell(0,6,table,"小数位");
            buildCell(0,7,table,"注释");

            for (Map<String, String> stringStringMap : fieldMap) {
                buildCell(j,0,table,stringStringMap.get("序号"));
                buildCell(j,1,table,stringStringMap.get("字段名"));
                buildCell(j,2,table,stringStringMap.get("类型"));
                buildCell(j,3,table,stringStringMap.get("长度"));
                buildCell(j,4,table,stringStringMap.get("是否为空"));
                buildCell(j,5,table,stringStringMap.get("默认值"));
                buildCell(j,6,table,stringStringMap.get("小数位"));
                buildCell(j,7,table,stringStringMap.get("注释"));
                j++;
            }
            // 添加空行
            document.createParagraph();

        }







        // 将文档写入文件
        try (FileOutputStream out = new FileOutputStream("D:\\io_test\\Example.docx")) {
            document.write(out);
        }
        document.close();
    }

    static void buildCell(int row,int cols,XWPFTable table,String value) {
        XWPFTableRow xwpfRow = table.getRow(row);
        XWPFTableCell xwpfCell = xwpfRow.getCell(cols);
        XWPFParagraph cellParagraph = xwpfCell.getParagraphs().get(0);
        XWPFRun cellRun = cellParagraph.createRun();
        cellRun.setText(value);
    }
}
