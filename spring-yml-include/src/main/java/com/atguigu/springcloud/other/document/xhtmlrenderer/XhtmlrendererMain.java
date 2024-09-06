package com.atguigu.springcloud.other.document.xhtmlrenderer;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

// 适合需要将 HTML/CSS 转换为 PDF 的场景。
public class XhtmlrendererMain {

    public static void main(String[] args) {
        try {
            OutputStream os = new FileOutputStream("E:\\file\\output.pdf");
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString("<html><body><h1>Hello World!</h1></body></html>");
            renderer.layout();
            renderer.createPDF(os);
            os.close();
            System.out.println("PDF created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
