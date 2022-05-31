package com.bigDragon.documentOperation.poi.excel;

import com.bigDragon.documentOperation.poi.excel.dto.AcHisTeamRptDto;
import com.bigDragon.documentOperation.poi.excel.util.ExcelExportUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.poi.ss.usermodel.CellType;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/1/13
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        List<AcHisTeamRptDto> acHisTeamRptDtos = new ArrayList<>();

        ExcelExportUtil.checkAndExport(response,acHisTeamRptDtos,"还款类历史团队报表.xls","Sheet",
                AcHisTeamRptDto.class,"还款类历史团队报表导出失败！");
    }

    //测试合并单元格
    @Test
    public void method1(){
        FileOutputStream out = null;
        try {
            //创建工作薄对象
            HSSFWorkbook workbook = new HSSFWorkbook();//这里也可以设置sheet的Name
            //创建工作表对象
            HSSFSheet sheet = workbook.createSheet();
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            int outPutNum = 0;

            //合并单元格
            CellRangeAddress region = new CellRangeAddress(1, 5, 1, 1);
            sheet.addMergedRegion(region);
            CellRangeAddress region2 = new CellRangeAddress(1, 4, 1, 1);
            sheet.addMergedRegion(region2);

            //基础10*10数据
            for(int i=0;i<10;i++){
                HSSFRow row = sheet.createRow(i);
                for(int j=0;j<10;j++){
                    HSSFCell cell = row.createCell(j, CellType.STRING);
                    cell.setCellValue(outPutNum+"");
                    cell.setCellStyle(style);
                    outPutNum++;
                }
            }

/*            //合并单元格
            CellRangeAddress region = new CellRangeAddress(1, 5, 1, 1);
            sheet.addMergedRegion(region);*/

  /*          region = new CellRangeAddress(0, 1, 1, 1);
            sheet.addMergedRegion(region);*/
            String filePath = "D:\\excel\\mergeCells\\";
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls";
            out = new FileOutputStream(filePath + fileName);
            workbook.write(out);
            System.out.println("文件已经生成，路径及文件名: "+filePath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
