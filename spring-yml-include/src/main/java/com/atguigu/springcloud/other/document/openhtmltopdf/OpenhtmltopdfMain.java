package com.atguigu.springcloud.other.document.openhtmltopdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// 先创建一个动态HTML文件，‌然后使用HTML转PDF的工具或库将其转换为PDF。‌（底层还是直接使用的pdfbox）
// 这种方法适用于需要从HTML内容生成PDF的情况。‌可以在Spring Boot应用程序中实现这种转换，‌例如通过将HTML内容保存为文件，‌然后使用外部工具或库将其转换为PDF。‌
// 在Spring Boot中，可以使用OpenPDF库（一个开源的iText分支）来动态生成PDF文件。
// 这个2021年之后maven主仓就不再维护了，其他仓库有维护，不建议使用。
public class OpenhtmltopdfMain {

    public static byte[] generatePdfFromHtml(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();

        builder.useFastMode();
        builder.withHtmlContent(htmlContent, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        String htmlContent = "<html><body><h1 style='color:red;text-align:center;'>Hello, World!</h1></body></html>";
        byte[] pdfBytes = generatePdfFromHtml(htmlContent);

        Files.write(Paths.get("E:\\file\\output.pdf"), pdfBytes);
        System.out.println("PDF SUCCESS");
    }

}
