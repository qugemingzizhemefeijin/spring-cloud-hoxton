package com.atguigu.springcloud.poi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractExcelView<T , K> extends AbstractXlsxView {

    /**
     * 标题
     */
    protected final String title;

    /**
     * 数据头信息
     */
    protected final List<ExcelColumnHead<K, Object>> headList;

    /**
     * 表单名称
     */
    protected final String sheetName;

    /**
     * 数据转换接口
     */
    protected final DataAware<T , K> dataAware;

    public AbstractExcelView(String title, String sheetName, List<ExcelColumnHead<K, Object>> headList, DataAware<T , K> dataAware) {
        this.title = title;
        this.sheetName = sheetName;
        this.headList = headList;
        this.dataAware = dataAware;
    }

    protected void createSheet(Workbook workbook, Sheet sheet, List<T> dataList) {
        int headSize = headList.size();
        Cell cell;
        for(int i = 0,size = dataList.size();i < size; i++) {
            T t = dataList.get(i);

            Row row = sheet.createRow(i + 1);
            row.setHeight(ExcelConstant.ROW_HEIGHT);

            K k = dataAware.parse(t);
            if (k != null) {
                for (int j = 0; j < headSize; j++) {
                    ExcelColumnHead<K, Object> head = headList.get(j);
                    Object value = head.value(k);
                    CellType cellType = head.getCellType();

                    if (value == null) {
                        if (cellType == CellType.NUMERIC) {
                            value = 0;
                        } else {
                            value = "";
                        }
                    }

                    cell = row.createCell(j);

                    if (cellType == CellType.STRING) {
                        if(value instanceof Date) {
                            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(value));
                        } else {
                            cell.setCellValue(String.valueOf(value));
                        }
                    } else if (cellType == CellType.NUMERIC && value instanceof Number) {//必须是数值型
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }

                    cell.getCellStyle().cloneStyleFrom(head.getStyleConvert().createStyle(workbook));
                }
            }
        }
    }

    /**
     * 生成头部信息
     * @param workbook - Workbook
     * @param sheet - Sheet
     */
    protected void createHead(Workbook workbook , Sheet sheet) {
        if(CollectionUtils.isEmpty(headList)) {
            return;
        }
        //建立表头
        Row row = sheet.createRow(0);
        row.setHeight(ExcelConstant.ROW_HEIGHT);
        for(int i=0 , size = headList.size();i<size;i++){
            ExcelColumnHead<K, Object> column = headList.get(i);

            Cell cell = row.createCell(i);
            cell.setCellValue(column.getName());
            cell.setCellStyle(ExcelConstant.getCellStyle(ExcelConstant.GREY_STYLE, workbook));

            sheet.setColumnWidth(i, column.getColumnWidth());
        }
    }

}
