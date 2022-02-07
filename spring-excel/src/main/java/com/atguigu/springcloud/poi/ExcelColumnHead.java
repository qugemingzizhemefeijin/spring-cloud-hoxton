package com.atguigu.springcloud.poi;

import lombok.Data;
import org.apache.poi.ss.usermodel.CellType;

import java.util.function.Function;

@Data
public class ExcelColumnHead<T , K> {

    //default的样式
    private static final CellStyleConvert DEFAULT_STYLE_CONVERT = ExcelConstant::getCellStyle;

    //带金钱符号的Double类型
    private static final CellStyleConvert MONEY_STYLE_CONVERT = (wb) -> ExcelConstant.getCellStyle(ExcelConstant.STYLE_MONEY, wb);

    //不带金钱符号的Double类型
    private static final CellStyleConvert MONEY_NO_SYMBOL_STYLE_CONVERT =  (wb) -> ExcelConstant.getCellStyle(ExcelConstant.STYLE_MONEY_NO_SYMBOL, wb);

    //千位符号的Number类型
    private static final CellStyleConvert NUMBER_STYLE_CONVERT =  (wb) -> ExcelConstant.getCellStyle(ExcelConstant.STYLE_NUMBER, wb);

    //带%号的类型
    private static final CellStyleConvert PERCENT_STYLE_CONVERT =  (wb) -> ExcelConstant.getCellStyle(ExcelConstant.STYLE_PERCENT, wb);

    /**
     * 列名称
     */
    private final String name;

    /**
     * 列宽度
     */
    private final int columnWidth;

    /**
     * 列类型
     */
    private final CellType cellType;

    /**
     * 将数据转换成excel的value值
     */
    private final Function<? super T, ? extends K> valueMapper;

    /**
     * poi的单元格样式规则
     */
    private final CellStyleConvert styleConvert;

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper){
        return new ExcelColumnHead<>(name , valueMapper, CellType.STRING);
    }

    /**
     * 创建一个带符号的金钱类型ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builderMoney(String name , Function<? super T, ? extends K> valueMapper){
        return new ExcelColumnHead<>(name , valueMapper , CellType.NUMERIC , MONEY_STYLE_CONVERT);
    }

    /**
     * 创建一个不带符号的金钱类型ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builderNoSymbolMoney(String name , Function<? super T, ? extends K> valueMapper){
        return new ExcelColumnHead<>(name , valueMapper , CellType.NUMERIC , MONEY_NO_SYMBOL_STYLE_CONVERT);
    }

    /**
     * 创建一个千位符号的数值类型ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builderNumber(String name , Function<? super T, ? extends K> valueMapper){
        return new ExcelColumnHead<>(name , valueMapper , CellType.NUMERIC , NUMBER_STYLE_CONVERT);
    }

    /**
     * 创建一个%号的数值类型ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builderPercentNumber(String name , Function<? super T, ? extends K> valueMapper){
        return new ExcelColumnHead<>(name , valueMapper , CellType.NUMERIC , PERCENT_STYLE_CONVERT);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param cellType - 单元格类型
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , CellType cellType){
        return new ExcelColumnHead<>(name , valueMapper , cellType);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param styleConvert - 样式转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , CellStyleConvert styleConvert){
        return new ExcelColumnHead<>(name , valueMapper , styleConvert);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param cellType - 单元格类型
     * @param styleConvert - 样式转换器
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , CellType cellType , CellStyleConvert styleConvert){
        return new ExcelColumnHead<>(name , valueMapper , cellType , styleConvert);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param columnWidth - 列宽度，最少5000
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , int columnWidth){
        return new ExcelColumnHead<>(name , valueMapper , columnWidth);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param cellType - 单元格类型
     * @param columnWidth - 列宽度，最少5000
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , CellType cellType , int columnWidth){
        return new ExcelColumnHead<>(name , valueMapper , cellType , columnWidth);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param styleConvert - 样式转换器
     * @param columnWidth - 列宽度，最少5000
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , CellStyleConvert styleConvert , int columnWidth){
        return new ExcelColumnHead<>(name , valueMapper , CellType.STRING , styleConvert , columnWidth);
    }

    /**
     * 创建一个ColumnHead
     * @param name - 列头名称
     * @param valueMapper - 值转换器
     * @param cellType - 单元格类型
     * @param styleConvert - 样式转换器
     * @param columnWidth - 列宽度，最少5000
     * @return ExcelColumnHead<T , K>
     */
    public static <T , K> ExcelColumnHead<T , K> builder(String name , Function<? super T, ? extends K> valueMapper , CellType cellType , CellStyleConvert styleConvert , int columnWidth){
        return new ExcelColumnHead<>(name , valueMapper , cellType , styleConvert , columnWidth);
    }

    /**
     * 构造
     * @param name - 列名称
     * @param valueMapper - 值转换
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper) {
        this(name , valueMapper , CellType.STRING , DEFAULT_STYLE_CONVERT , ExcelConstant.COLUMN_WIDTH);
    }

    /**
     * 构造
     * @param name - 列名称
     * @param valueMapper - 值转换
     * @param cellType - 单元格类型
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper , CellType cellType) {
        this(name , valueMapper , cellType , DEFAULT_STYLE_CONVERT , ExcelConstant.COLUMN_WIDTH);
    }

    /**
     * 构造
     * @param name - 列头名称
     * @param valueMapper - 值
     * @param columnWidth - 列宽度，最少5000
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper , int columnWidth) {
        this(name , valueMapper , CellType.STRING , DEFAULT_STYLE_CONVERT , columnWidth);
    }

    /**
     * 构造
     * @param name - 列头名称
     * @param valueMapper - 值
     * @param cellType - 单元格类型
     * @param columnWidth - 列宽度，最少5000
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper , CellType cellType , int columnWidth) {
        this(name , valueMapper , cellType , DEFAULT_STYLE_CONVERT , columnWidth);
    }

    /**
     * 构造
     * @param name - 列头名称
     * @param valueMapper - 值
     * @param styleConvert - 样式转换器
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper , CellStyleConvert styleConvert) {
        this(name , valueMapper , CellType.STRING , styleConvert , ExcelConstant.COLUMN_WIDTH);
    }

    /**
     * 构造
     * @param name - 列头名称
     * @param valueMapper - 值
     * @param cellType - 单元格类型
     * @param styleConvert - 样式转换器
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper , CellType cellType , CellStyleConvert styleConvert) {
        this(name , valueMapper , cellType , styleConvert , ExcelConstant.COLUMN_WIDTH);
    }

    /**
     * 构造
     * @param name - 列头名称
     * @param valueMapper - 值
     * @param cellType - 单元格类型
     * @param styleConvert - 样式转换器
     * @param columnWidth - 列宽度，最少5000
     */
    private ExcelColumnHead(String name , Function<? super T, ? extends K> valueMapper , CellType cellType , CellStyleConvert styleConvert , int columnWidth) {
        if(columnWidth < ExcelConstant.COLUMN_WIDTH) {
            columnWidth = ExcelConstant.COLUMN_WIDTH;
        }
        if(styleConvert == null) {
            styleConvert = DEFAULT_STYLE_CONVERT;
        }
        if(cellType == null) {
            cellType = CellType.STRING;
        }

        this.name = name;
        this.valueMapper = valueMapper;
        this.cellType = cellType;
        this.styleConvert = styleConvert;
        this.columnWidth = columnWidth;
    }

    public K value(T t) {
        return valueMapper.apply(t);
    }

}
