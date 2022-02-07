package com.atguigu.springcloud.poi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ExcelView<T , K> extends AbstractExcelView<T, K> {

    /**
     * 数据信息
     */
    private final List<T> dataList;

    /**
     * 构造函数
     * @param title - 导出文件标题
     * @param dataList - 导出列头
     */
    public ExcelView(String title , List<ExcelColumnHead<K , Object>> headList , List<T> dataList){
        this(title , null , headList , dataList , t -> (K)t);
    }

    /**
     * 构造函数
     * @param title - 导出文件标题
     * @param dataList - 导出列头
     * @param dataAware - 数据信息
     */
    public ExcelView(String title , List<ExcelColumnHead<K , Object>> headList , List<T> dataList , DataAware<T , K> dataAware){
        this(title , null , headList , dataList , dataAware);
    }


    /**
     * 构造函数
     * @param title - 导出文件标题
     * @param sheetName - 表单名称
     * @param headList - 导出列头
     * @param dataList - 数据信息
     */
    public ExcelView(String title , String sheetName , List<ExcelColumnHead<K , Object>> headList , List<T> dataList , DataAware<T , K> dataAware){
        super(title, sheetName, headList, dataAware);
        this.dataList = dataList;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //生成Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(title, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        ServletOutputStream sos = null;
        try {
            //生成表单
            Sheet sheet = workbook.createSheet(sheetName == null ? "数据" : sheetName);
            createHead(workbook , sheet);
            //生成数据部分
            if(!CollectionUtils.isEmpty(dataList)) {
                createSheet(workbook, sheet, dataList);
            }

            //输出数据
            sos = response.getOutputStream();
            sos.flush();
            workbook.write(sos);
            response.flushBuffer();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            //清除线程变量
            ExcelConstant.clearThreadLocal();
        }
    }

}
