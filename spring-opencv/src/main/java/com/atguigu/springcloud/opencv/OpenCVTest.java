package com.atguigu.springcloud.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

// https://blog.csdn.net/ZYH_Victor/article/details/140277961
// opencv 4.10.0 需要JDK11
public class OpenCVTest {

    public static void main(String[] args) {
        //加载 opencv_javaXX.dll
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //测试图片地址
        String imgPath = "C:\\Users\\a\\Desktop\\aaa.jpg";
        //读取图片
        Mat img = Imgcodecs.imread(imgPath);
        //生成灰度图
        Mat gray = new Mat();
        Imgproc.cvtColor(img,gray, Imgproc.COLOR_BGR2GRAY);
        //保存图片
        Imgcodecs.imwrite("E:/result.jpg", gray);
    }

}
