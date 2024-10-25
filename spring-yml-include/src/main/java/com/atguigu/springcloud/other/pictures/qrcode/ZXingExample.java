package com.atguigu.springcloud.other.pictures.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// 由于其功能更全面，ZXing 的实现可能稍复杂，性能上可能会受到解码过程的影响，但在生成二维码方面表现良好。
public class ZXingExample {

    public static void main(String[] args) throws WriterException, IOException {
        String data = "Hello, World!";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        Path path = Paths.get("E:/QRCode.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

}
