package com.atguigu.springcloud.other.pdfbox;

import com.atguigu.springcloud.other.bufferedimage.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        try {
            outputStreamList = pdf2Images(new FileInputStream(pdfPath), 3.0f, maxPage);
            if (outputStreamList == null) {
                return;
            }
            for (int i = 0, size = outputStreamList.size(); i < size; i++) {
                try (ByteArrayOutputStream temp = outputStreamList.get(i);) {
                    String writePath = outPath;
                    if (maxPage == 1) {
                        writePath = ImageUtils.getImagePath(outPath, String.valueOf(i));
                    }

                    FileUtils.writeByteArrayToFile(new File(writePath), temp.toByteArray());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<ByteArrayOutputStream> pdf2Images(InputStream is, float scale, int maxPage) {
        if (maxPage <= 0) {
            maxPage = Integer.MAX_VALUE;
        }

        try {
            PDDocument document = PDDocument.load(is);
            PDFRenderer renderer = new PDFRenderer(document);

            int pages = document.getNumberOfPages();
            if (pages <= 0) {
                return null;
            }

            List<ByteArrayOutputStream> outputStreamList = new ArrayList<>(pages);
            for (int i = 0; i < pages && i <= maxPage; i++) {
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
