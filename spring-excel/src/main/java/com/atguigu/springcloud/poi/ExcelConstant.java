package com.atguigu.springcloud.poi;

import com.google.common.collect.Maps;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.Map;

public final class ExcelConstant {

    /**
     * 列宽度
     */
    public static final int COLUMN_WIDTH = 4000;

    /**
     * 行高
     */
    public static final short ROW_HEIGHT = 500;

    /**
     * 正常带边框格式
     */
    public static final int NORMAL_STYLE = 0;

    /**
     * 灰色背景带边框格式
     */
    public static final int GREY_STYLE = 1;

    /**
     * 金钱格式
     */
    public static final int STYLE_MONEY = 2;

    /**
     * 日期格式
     */
    public static final int STYLE_DATE = 3;

    /**
     * 百分比格式
     */
    public static final int STYLE_PERCENT = 4;

    /**
     * 不带货币符号的金钱格式
     */
    public static final int STYLE_MONEY_NO_SYMBOL = 5;

    /**
     * 千位符数字格式
     */
    public static final int STYLE_NUMBER = 6;

    /**
     * 线程变量
     */
    private static final ThreadLocal<Map<Integer , CellStyle>> STYLE_LOCAL = new ThreadLocal<>();

    /**
     * 清空线程变量
     */
    public static void clearThreadLocal(){
        STYLE_LOCAL.remove();
    }

    /**
     * 根据Code获取样式
     * @param workbook - Workbook
     * @return CellStyle
     */
    public static CellStyle getCellStyle(Workbook workbook) {
        return getCellStyle(NORMAL_STYLE , workbook);
    }

    /**
     * 根据Code获取样式
     * @param code - int
     * @param workbook - Workbook
     * @return CellStyle
     */
    public static CellStyle getCellStyle(int code , Workbook workbook) {
        Map<Integer , CellStyle> map = STYLE_LOCAL.get();
        if(map == null) {
            map = Maps.newHashMap();
            STYLE_LOCAL.set(map);
        }

        CellStyle style = map.get(code);
        if(style == null) {
            switch(code) {
                case STYLE_MONEY:
                    style = createMoneyBorderStyle(workbook);
                    break;
                case STYLE_MONEY_NO_SYMBOL :
                    style = createMoneyNoSymbolBorderStyle(workbook);
                    break;
                case STYLE_NUMBER :
                    style = createNumberBorderStyle(workbook);
                    break;
                case STYLE_DATE:
                    style = createDateBorderStyle(workbook);
                    break;
                case STYLE_PERCENT:
                    style = createPercentBorderStyle(workbook);
                    break;
                case GREY_STYLE:
                    style = createGreyBorderStyle(workbook);
                    break;
                default :
                    style = createWhiteBorderStyle(workbook);
            }

            map.put(code, style);
        }

        return style;
    }

    /**
     * 创建货币边框
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createMoneyBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("￥#,##0.00"));

        return style;
    }

    /**
     * 创建不带￥符号的货币边框
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createMoneyNoSymbolBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));

        return style;
    }

    /**
     * 创建千位符号的货币边框
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createNumberBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));

        return style;
    }

    /**
     * 创建日期边框
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createDateBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy年m月d日"));

        return style;
    }

    /**
     * 创建百分比边框
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createPercentBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00%"));

        return style;
    }

    /**
     * 创建灰色背景，黑色边框的样式
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createGreyBorderStyle(Workbook workbook) {
        //灰色背景
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    /**
     * 创建白色背景，黑色边框的样式
     * @param workbook - Workbook
     * @return CellStyle
     */
    private static CellStyle createWhiteBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        return style;
    }

}
