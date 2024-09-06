package com.atguigu.springcloud.other.document.itextpdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

// 功能丰富，适合需要精确控制 PDF 内容的场景。
public class ItextpdfMain {

    public static void main(String[] args) {
        try {
            PdfWriter writer = new PdfWriter("E:\\file\\output.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("Hello World!"));
            document.close();
            System.out.println("PDF created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
