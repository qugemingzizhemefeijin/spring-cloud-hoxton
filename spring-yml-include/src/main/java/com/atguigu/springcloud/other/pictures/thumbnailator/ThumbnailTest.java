package com.atguigu.springcloud.other.pictures.thumbnailator;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

public class ThumbnailTest {

    public static void main(String[] args) throws IOException {
        // 按比例缩放图像
        // Thumbnails.of("E:/result_image.png").scale(0.5).toFile("E:/output.jpg");
        // 指定缩略图的宽高(不加keepAspectRatio(false))默认是按比例
        // Thumbnails.of("E:/result_image.png").size(200, 200).keepAspectRatio(false).toFile("E:/output1.jpg");
        // 指定最大宽高并保持比例
        // Thumbnails.of("E:/result_image.png").size(800, 800).keepAspectRatio(true).toFile("E:/output2.jpg");
        // 设置输出格式和质量
        Thumbnails.of("E:/result_image.png").size(200, 200).outputFormat("jpg").outputQuality(0.8).toFile("E:/output.jpg");
    }

}
