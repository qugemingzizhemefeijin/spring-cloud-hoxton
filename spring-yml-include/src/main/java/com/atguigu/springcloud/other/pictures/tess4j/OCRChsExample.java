package com.atguigu.springcloud.other.pictures.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRChsExample {

    public static void main(String[] args) {
        // 创建 Tesseract 实例
        ITesseract tesseract = new Tesseract();

        // 设置 Tesseract 的数据目录路径
        tesseract.setDatapath("D:\\Program Files\\Tesseract-OCR\\tessdata"); // Tesseract 数据目录的实际路径

        // 设置语言（例如英文），可以指定多个语言，用 "+" 连接（例如 "eng+chi_sim"）
        tesseract.setLanguage("chi_sim+eng"); // 也可以使用其他语言代码

        // 指定要识别的图像文件
        File imageFile = new File("C:\\Users\\a\\Desktop\\chs_simple.png"); // 替换为你要识别的图像文件路径

        try {
            // 执行 OCR 识别
            String result = tesseract.doOCR(imageFile);

            // 输出识别结果
            System.out.println("识别结果: " + result);

        } catch (TesseractException e) {
            System.err.println("识别失败: " + e.getMessage());
        }
    }

}
