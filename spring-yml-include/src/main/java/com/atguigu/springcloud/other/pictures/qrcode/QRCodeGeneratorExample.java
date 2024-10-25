package com.atguigu.springcloud.other.pictures.qrcode;

import com.atguigu.springcloud.other.pictures.bufferedimage.ImageUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import net.glxn.qrgen.javase.QRCode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

// 相对简单，性能优越于仅生成二维码的场景。
public class QRCodeGeneratorExample {

    public static void main(String[] args) throws Exception {
        // test1();

        int size = 200;
        Image i = test2(size);

        Image image = Toolkit.getDefaultToolkit().createImage(new URL("https://dss1.bdstatic.com/lvoZeXSm1A5BphGlnYG/skin/12.jpg?2"));
        BufferedImage bgImg = ImageUtils.toBufferedImage(image);

        Graphics2D g = bgImg.createGraphics();
        // 设置字体抗锯齿
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.drawImage(i, 0, 0, size, size, null);
        g.dispose();

        ImageUtils.saveImageJPG(bgImg, "E:/aaa.jpg");
    }

    private static Image test2(int size) throws WriterException {
        String content = "Hello World";
        QRCode qrcode = QRCode.from(content);

        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BufferedImage qimg = MatrixToImageWriter.toBufferedImage(qrcode.getQrWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints));

        ImageFilter cropFilter = getCropImage(qimg, size, true);

        return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(qimg.getSource(), cropFilter));
    }

    private static CropImageFilter getCropImage(BufferedImage qimg, int size, boolean hasEdge) {
        int e = 0;
        for (int i = 0; i < size / 4; i++) {
            int rgb = qimg.getRGB(i, i);
            if (rgb != -1) {
                e = i - 1;
                break;
            }
        }
        int x = e;
        int y = e * 5 / 6;
        int a = size - x * 2;
        int b = size - y * 2;
        // 带白边
        if (hasEdge) {
            return new CropImageFilter(y, y, b, b);
        } else {
            return new CropImageFilter(x, x, a, a);
        }
    }

    private static void test1() {
        String data = "Hello, World!";
        File qrCodeFile = QRCode.from(data).withSize(200, 200).file();
        System.out.println("QRCode generated at: " + qrCodeFile.getPath());

        File outputFile = new File("E:/", "qcord.png");
        if (qrCodeFile.renameTo(outputFile)) {
            System.out.println("二维码成功生成并保存到: " + outputFile.getAbsolutePath());
        } else {
            System.out.println("生成二维码失败!");
        }
    }

}
