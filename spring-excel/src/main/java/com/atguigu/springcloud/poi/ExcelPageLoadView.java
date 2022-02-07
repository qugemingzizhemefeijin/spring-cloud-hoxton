package com.atguigu.springcloud.poi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 分页加载的Excel视图，防止数据太多造成内存溢出
 * @param <K>
 */
@Getter
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ExcelPageLoadView<T, K> extends AbstractExcelView<T, K> {

    private static final int DEFAULT_PAGE_SIZE = 200;

    /**
     * 数据加载接口
     */
    private final DataLoad<T> dataLoad;

    /**
     * 每页查询200条数据
     */
    private final int pageSize;

    /**
     * 构造函数
     * @param title - 导出文件标题
     * @param headList - 导出列头
     * @param dataLoad - 数据加载
     */
    public ExcelPageLoadView(String title , List<ExcelColumnHead<K , Object>> headList , DataLoad<T> dataLoad){
        this(title, null, headList, t -> (K)t, dataLoad, DEFAULT_PAGE_SIZE);
    }

    /**
     * 构造函数
     * @param title - 导出文件标题
     * @param headList - 导出列头
     * @param dataLoad - 数据加载
     */
    public ExcelPageLoadView(String title , List<ExcelColumnHead<K , Object>> headList , DataAware<T , K> dataAware , DataLoad<T> dataLoad){
        this(title, null, headList, dataAware, dataLoad, DEFAULT_PAGE_SIZE);
    }

    /**
     * 构造函数
     * @param title - 导出文件标题
     * @param sheetName - 表单名称
     * @param headList - 导出列头
     * @param dataLoad - 数据加载
     */
    public ExcelPageLoadView(String title , String sheetName , List<ExcelColumnHead<K , Object>> headList , DataAware<T , K> dataAware , DataLoad<T> dataLoad , int pageSize){
        super(title, sheetName, headList, dataAware);
        this.dataLoad = dataLoad;
        this.pageSize = pageSize;
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

            int start = 0;
            while(true) {
                List<T> dataList = dataLoad.load(start, pageSize);

                if(dataList == null || dataList.isEmpty()) {
                    break;
                }
                createSheet(workbook, sheet, dataList);
                if(dataList.size() < pageSize) {
                    break;
                }
                start += pageSize;
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
