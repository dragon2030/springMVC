package com.bigDragon.javase.ioStream.excel.poi.excel.util;

import cn.hutool.core.collection.CollUtil;
import com.bigDragon.javase.ioStream.excel.poi.excel.common.CommonConstant;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
     * @param reportName 导出报表名称
     * @param sheetName 导出报表sheet名称
     * @param dtoClass 导出对象Class类
     * @param list  导出对象数据Collection
     * @param excludeColumn 需要动态排除的列
     * @param titleWorkbook 设置表头，主要针对于需要合并同类项的多行表头
     */
    public static void checkAndExport(String reportName, String sheetName, Class<?> dtoClass, List<?> list ,
                                      String[] excludeColumn, Workbook titleWorkbook){
        //文件名转义
        String fileName = null;
        try {
            fileName = reportName + ".xlsx";
            fileName = URLEncoder.encode(fileName, CommonConstant.ENCODE_UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            fileName = "report.xlsx";
        }

        //获取输出流
        OutputStream outputStream = null;
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setContentType(CommonConstant.CONENTTYPE_EXCEL);
            response.setHeader(CommonConstant.RESHEADER_CONTENTDISPOSITION,
                    CommonConstant.CONTENTDISPOSITION_PREFIX + fileName);
            response.setHeader(CommonConstant.RES_INTERCEPTOR, Boolean.FALSE.toString());
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("HTTP相应IO流错误");
        }
        //Excel文件导出
        try {
            //非空判断,空数据时，返回完整表头的excel数据
            if(CollUtil.isEmpty(list)){
                ExcelUtil.exportExcelTitle(sheetName, dtoClass, outputStream, excludeColumn, titleWorkbook);
            }
            ExcelUtil.exportExcel(sheetName, dtoClass, list, outputStream, excludeColumn, titleWorkbook);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(reportName+"导出失败！", e);
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
