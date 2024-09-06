package com.atguigu.springcloud.other.document.commonmark;

import com.atguigu.springcloud.other.document.wkhtml.HtmlToPdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MarkdownToPdf {

    public static final String WINDOWS_FILE_URL = "E:\\file";

    public static void main(String[] args) {
        try {
            String bashPath = "E:\\mygithub\\spring-cloud-hoxton\\spring-yml-include\\src\\main\\java\\com\\atguigu\\springcloud\\other\\commonmark\\document";
            String testFilePath = bashPath + "\\test.md";

            String destFileName = WINDOWS_FILE_URL + "\\output.pdf";

            File file = new File(destFileName);
            File parent = file.getParentFile();
            //如果pdf保存路径不存在，则创建路径
            if (!parent.exists()) {
                parent.mkdirs();
            }

            String htmlHeader = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "<title>Markdown to HTML</title>\n" +
                    "<style>\n" +
                    "body {\n" +
                    "    font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif;\n" +
                    "    line-height: 1.5;\n" +
                    "    color: #24292e;\n" +
                    "    background-color: #f6f8fa;\n" +
                    "}\n" +
                    "h1, h2, h3, h4, h5, h6 {\n" +
                    "    border-bottom: 1px solid #e1e4e8;\n" +
                    "}\n" +
                    "a {\n" +
                    "    color: #0366d6;\n" +
                    "    text-decoration: none;\n" +
                    "}\n" +
                    "a:hover {\n" +
                    "    text-decoration: underline;\n" +
                    "}\n" +
                    "code {\n" +
                    "    background-color: #f1f1f1;\n" +
                    "    padding: 2px 4px;\n" +
                    "    border-radius: 4px;\n" +
                    "}\n" +
                    "pre {\n" +
                    "    background-color: #f6f8fa;\n" +
                    "    padding: 16px;\n" +
                    "    border-radius: 6px;\n" +
                    "    overflow: auto;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>";

            String htmlFooter = "</body>\n" +
                    "</html>";

            // 读取 Markdown 文件
            String markdownContent = new String(Files.readAllBytes(Paths.get(testFilePath)));

            // 解析 Markdown 内容
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String htmlContent = renderer.render(parser.parse(markdownContent));

            // 创建 PDF 文档（这段代码实际就是将解析出来的html原样生成到PDF中，包含了html标签了）
            try (PDDocument document = new PDDocument()) {
                InputStream fontStream = new FileInputStream(bashPath + "\\hyxl.ttf");
                PDType0Font font = PDType0Font.load(document, fontStream);

                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(25, 750);
                    contentStream.showText(htmlContent);
                    contentStream.endText();
                }
                document.save(destFileName);
            }

            // 需要配合wkhtmltopdf来将 HTML 到 PDF 转换.
            String htmlPath = HtmlToPdfUtils.stringToHtml(htmlHeader + htmlContent + htmlFooter);

            HtmlToPdfUtils.convert(htmlPath, "markdown.pdf");

            Files.delete(Paths.get(htmlPath));


            System.out.println("PDF 创建成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
