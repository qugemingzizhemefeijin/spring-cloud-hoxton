package com.atguigu.springcloud.other.document.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// PDFBox是一个开源的Java库，‌用于处理PDF文档。‌它支持创建、‌读取和修改PDF文件。
// ‌在Spring Boot应用程序中，‌可以通过PDFBox库来生成PDF文件。
// ‌具体实现包括创建一个PDDocument对象，‌添加页面，‌设置页面内容流，‌设置字体和大小，‌显示文本，‌最后保存并关闭文档。‌
// 开源且功能强大，适合处理 PDF 文档的各种操作。
@Slf4j
public final class PdfUtils {

    public static void main(String[] args) {
        PdfUtils.pdf2Images("E:\\乱七八糟\\社保对账单\\2023个人对账单.pdf", "E:\\乱七八糟\\社保对账单\\aaaa.jpg", 1);
    }

    public static void pdf2Images(String pdfPath, String outPath) {
        pdf2Images(pdfPath, outPath, 1);
    }

    public static void pdf2Images(String pdfPath, String outPath, int maxPage) {
        List<ByteArrayOutputStream> outputStreamList;
        outputStreamList = pdf2Images(pdfPath, 3.0f, maxPage);
        if (outputStreamList == null) {
            return;
        }
        for (int i = 0, size = outputStreamList.size(); i < size; i++) {
            try (ByteArrayOutputStream temp = outputStreamList.get(i);) {
                String writePath = outPath;
                if (maxPage == 1) {
                    writePath = outPath;
                }

                FileUtils.writeByteArrayToFile(new File(writePath), temp.toByteArray());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ByteArrayOutputStream> pdf2Images(String fileName, float scale, int maxPage) {
        if (maxPage <= 0) {
            maxPage = Integer.MAX_VALUE;
        }

        try {
            PDDocument document = Loader.loadPDF(new File(fileName));
            PDFRenderer renderer = new PDFRenderer(document);

            int pages = document.getNumberOfPages();
            if (pages <= 0) {
                return null;
            }

            List<ByteArrayOutputStream> outputStreamList = new ArrayList<>(pages);
            for (int i = 0; i < pages && i < maxPage; i++) {
                BufferedImage image = renderer.renderImage(i, scale);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "JPEG", baos);
                image.flush();

                outputStreamList.add(baos);
            }

            return outputStreamList;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private PdfUtils() {
        throw new AssertionError("No Instance.");
    }

}
