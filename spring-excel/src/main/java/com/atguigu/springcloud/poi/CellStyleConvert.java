package com.atguigu.springcloud.poi;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

@FunctionalInterface
public interface CellStyleConvert {

    CellStyle createStyle(Workbook workbook);

}
