package com.atguigu.springcloud.other.document.fontbox;

import org.apache.commons.io.FileUtils;
import org.apache.fontbox.ttf.CmapSubtable;
import org.apache.fontbox.ttf.CmapTable;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.pdfbox.io.RandomAccessReadBuffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TTFExtractorTest {

    public static void main(String[] args) {
        String ttfPath = "C:\\Users\\a\\Desktop\\fontscn_yx77i032.ttf";
        String outputDir = "E:\\乱七八糟\\output_images\\";

        try (FileInputStream fis = new FileInputStream(ttfPath)) {
            // 1. 加载 TTF 文件并解析
            TTFParser parser = new TTFParser();
            TrueTypeFont font = parser.parse(new RandomAccessReadBuffer(fis));

            // 2. 获取所有支持的 Unicode 字符编码（通过 CMAP 表）
            Set<Integer> codePoints = extractUnicodeCodePoints(font);

            // 3. 创建 AWT 字体对象（用于渲染）
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, new File(ttfPath)).deriveFont(64f); // 初始字体大小

            // 4. 创建输出目录
            File outputDirFile = new File(outputDir);
            FileUtils.deleteDirectory(outputDirFile);
            outputDirFile.mkdir();

            // 5. 遍历每个字符并生成图片
            for (int codePoint : codePoints) {
                String charStr = new String(Character.toChars(codePoint));
                String fileName = String.format("U+%04X.png", codePoint);
                generateCharacterImage(awtFont, charStr, outputDir + fileName);
            }

            // 获取出图片之后，可以通过OCR工具识别图片数值，再返回unicode和数值的映射关系
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    // 提取 Unicode 码点（直接遍历编码范围）
    private static Set<Integer> extractUnicodeCodePoints(TrueTypeFont font) throws IOException {
        Set<Integer> codePoints = new HashSet<>();
        CmapTable cmapTable = font.getCmap();
        if (cmapTable == null) return codePoints;

        // 遍历所有 CMAP 子表
        for (CmapSubtable subtable : cmapTable.getCmaps()) {
            // 仅处理 Unicode 编码的子表（Platform 3）
            if (subtable.getPlatformId() == CmapTable.PLATFORM_WINDOWS ||
                    subtable.getPlatformId() == CmapTable.PLATFORM_UNICODE) {

                // 遍历可能的字符编码范围（BMP 范围：0x0000-0xFFFF）
                for (int charCode = 0x0000; charCode <= 0xFFFF; charCode++) {
                    try {
                        int glyphId = subtable.getGlyphId(charCode);
                        if (glyphId != 0) { // 过滤无效字形
                            codePoints.add(charCode);
                        }
                    } catch (Exception e) {
                        // 忽略无效字符编码
                    }
                }
            }
        }
        return codePoints;
    }

    // 生成字符图片
    private static void generateCharacterImage(Font font, String character, String outputPath) {
        int width = 100;
        int height = 100;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // 配置抗锯齿和字体
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        // 计算居中位置
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (width - metrics.stringWidth(character)) / 2;
        int y = (height - metrics.getHeight()) / 2 + metrics.getAscent();

        // 绘制并保存
        g2d.drawString(character, x, y);
        g2d.dispose();

        try {
            ImageIO.write(image, "PNG", new File(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
