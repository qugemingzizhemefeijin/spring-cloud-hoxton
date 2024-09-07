package com.atguigu.springcloud.opencv;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

// 人脸识别
// https://blog.csdn.net/qq_54693844/article/details/134465092
// https://blog.csdn.net/yueritme/article/details/139698537
public class FaceCheckTest {

    static {
        //加载 opencv_javaXX.dll
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // 读取图像
        Mat image = Imgcodecs.imread(FaceCheckTest.class.getResource("/img/face.jpg").getPath().substring(1));

        // 灰度图
        Mat targetGrayImage = new Mat();
        Imgproc.cvtColor(image, targetGrayImage, Imgproc.COLOR_BGR2GRAY);

        // # haarcascade_eye                        睁开的眼睛的检测
        // # haarcascade_eye_tree_eyeglasses        戴眼镜时睁开的眼睛
        // # haarcascade_frontalcatface             正脸检测
        // # haarcascade_frontalcatface_extended    正脸检测
        // # haarcascade_frontalface_alt            正脸检测
        // # haarcascade_frontalface_alt_tree       正脸检测
        // # haarcascade_frontalface_alt2           正脸检测
        // # haarcascade_frontalface_default        正脸检测
        // # haarcascade_fullbody                   全身检测
        // # haarcascade_lefteye_2splits            检测左眼开或闭合
        // # haarcascade_licence_plate_rus_16stages 俄罗斯车牌
        // # haarcascade_lowerbody                  下肢检测
        // # haarcascade_profileface                人脸轮廓检测
        // # haarcascade_righteye_2splits           检测右眼开或闭合
        // # haarcascade_russian_plate_number       俄罗斯车牌号
        // # haarcascade_smile                      微笑表情检测
        // # haarcascade_upperbody                  上半身检测
        // classfier = cv2.CascadeClassifier("haarcascade_frontalface_alt2.xml")
        // # detectMultiScale函数。可以检测出图片中所有的人脸，并将人脸用vector保存各个人脸的坐标、大小（用矩形表示），函数由分类器对象调用
        // # 参数1：image--待检测图片，一般为灰度图像加快检测速度；
        // # 参数2：objects--被检测物体的矩形框向量组；
        // # 参数3：scaleFactor--表示在前后两次相继的扫描中，搜索窗口的比例系数。默认为1.1即每次搜索窗口依次扩大10%;
        // # 参数4：minNeighbors--表示构成检测目标的相邻矩形的最小个数(默认为3个)。
        // #         如果组成检测目标的小矩形的个数和小于 min_neighbors - 1 都会被排除。
        // #         如果min_neighbors 为 0, 则函数不做任何操作就返回所有的被检候选矩形框，
        // #         这种设定值一般用在用户自定义对检测结果的组合程序上；
        // # 参数5：flags--要么使用默认值，要么使用CV_HAAR_DO_CANNY_PRUNING，如果设置为
        // #         CV_HAAR_DO_CANNY_PRUNING，那么函数将会使用Canny边缘检测来排除边缘过多或过少的区域，
        // #         因此这些区域通常不会是人脸所在区域；
        // # 参数6、7：minSize和maxSize用来限制得到的目标区域的范围

        // 加载人脸分类器
        CascadeClassifier faceDetector = new CascadeClassifier(FaceCheckTest.class.getResource("/haarcascades/haarcascade_frontalface_alt2.xml").getPath().substring(1));

        // 人脸检测
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(targetGrayImage, faceDetections);

        // 绘制人脸框
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // 显示结果
        HighGui.imshow("Detected Face", image);
        HighGui.waitKey();

        // 释放资源
        image.release();
        targetGrayImage.release();

        HighGui.destroyAllWindows();

        System.out.println("Over");
        System.exit(0);
    }

}
