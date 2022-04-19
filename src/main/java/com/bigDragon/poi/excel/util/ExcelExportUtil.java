package com.bigDragon.poi.excel.util;

import com.bigDragon.WebService.JsArray;
import com.bigDragon.poi.excel.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/1/13
 * @Description:
 *
 *      Excel导出工具类
 */
public class ExcelExportUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);

    /**
     * 创建Excel 写入输出流
     * @param response
     * @param list
     * @param fileName
     * @param sheetName
     * @param dtoClass
     * @param errorMsg
     */
    public static void checkAndExport(HttpServletResponse response, List<?> list,String fileName,
                                      String sheetName,Class<?> dtoClass,String errorMsg){
        //非空判断
        if(list == null || list.isEmpty()){
            throw new RuntimeException(Constant.REPORT_DATE_NULL);
        }
        //文件名转义
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            fileName = "report.xls";
        }

        //获取输出流
        OutputStream outputStream = null;
        response.setContentType("application/msexcel");
        response.setHeader("Context-disposition","attachment;filename="+fileName);
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(Constant.HTTP_IO_ERROR);
        }
        //Excel文件导出
        try {
            ExcelUtil.exportExcel(sheetName,dtoClass,list,outputStream);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(errorMsg);
        }finally {
            if (null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }finally {
                    outputStream = null;
                }
            }
        }

    }
}
