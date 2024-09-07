package com.atguigu.springcloud.opencv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 基于图片模版进行图片检测
 */
public class CheckImageTemplateTest {

    static {
        //加载 opencv_javaXX.dll
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        //读取图片
        Mat targetImage = Imgcodecs.imread(CheckImageTemplateTest.class.getResource("/img/target_image2.png").getPath().substring(1));
        //读取图片
        Mat templateImage = Imgcodecs.imread(CheckImageTemplateTest.class.getResource("/img/template.png").getPath().substring(1));

        // 执行多尺度模板匹配
        double[] result = matchTemplateScaled(targetImage, templateImage, 0.5, 1.5, 0.1, 0.5);
        if (result[0] != -1) {
            System.out.printf("存在，匹配值: %.2f，缩放比例: %.2f%n", result[5], result[4]);
            Point topLeft = new Point(result[0], result[1]);
            Point bottomRight = new Point(result[0] + result[2], result[1] + result[3]);

            Imgproc.rectangle(targetImage, topLeft, bottomRight, new Scalar(0, 255, 0), 2);
            Imgcodecs.imwrite("E:/result_image.png", targetImage);
        } else {
            System.out.println("未找到");
        }
    }

    public static double[] matchTemplateScaled(Mat targetImage, Mat templateImage, double minScale, double maxScale, double step, double matchScore) {
        Mat targetGrayImage = new Mat();
        Mat templateGrayImage = new Mat();

        Imgproc.cvtColor(targetImage, targetGrayImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(templateImage, templateGrayImage, Imgproc.COLOR_BGR2GRAY);

        double bestVal = -1;
        double bestScale = 1;
        double[] bestMatch = {-1, -1, -1, -1};

        for (double scale = minScale; scale <= maxScale; scale += step) {
            Size newSize = new Size(templateGrayImage.cols() * scale, templateGrayImage.rows() * scale);
            Mat resizedTemplate = new Mat();
            Imgproc.resize(templateGrayImage, resizedTemplate, newSize);

            // System.out.println("Resized Image Size: Width = " + resizedTemplate.cols() + ", Height = " + resizedTemplate.rows());
            // System.out.println("Expected Size: Width = " + newSize.width + ", Height = " + newSize.height + ", scale = " + scale);

            Mat result = new Mat();
            Imgproc.matchTemplate(targetGrayImage, resizedTemplate, result, Imgproc.TM_CCOEFF_NORMED);
            Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

            if (minMaxLocResult.maxVal > bestVal) {
                // maxVal 它表示在模板匹配结果图像中的最大值。这个值指示了模板与目标图像的最佳匹配度，值越大表示匹配度越高。
                // 在模板匹配中，minVal 的意义取决于所使用的匹配方法。不同的匹配方法对 minVal 和 maxVal 的解释有所不同：
                // 1.相关系数匹配 (TM_CCOEFF 和 TM_CCOEFF_NORMED):
                //   这里 maxVal 表示最好的匹配度，值越高表示匹配度越好。minVal 在这种情况下可能不是特别有用，因为它更多地用于其他方法。
                // 2.平方差匹配 (TM_SQDIFF 和 TM_SQDIFF_NORMED):
                //   在这些方法中，minVal 表示最好的匹配度。平方差方法的目标是最小化模板和目标图像之间的差异，因此最小值表示最佳匹配。
                // 总结来说，minVal 和 maxVal 的用处依赖于匹配方法的不同：
                // 对于 TM_SQDIFF 和 TM_SQDIFF_NORMED，minVal 是关键指标，表示最佳匹配。
                // 对于 TM_CCOEFF 和 TM_CCOEFF_NORMED，maxVal 是关键指标，表示最佳匹配。
                bestVal = minMaxLocResult.maxVal;
                bestMatch[0] = minMaxLocResult.maxLoc.x;
                bestMatch[1] = minMaxLocResult.maxLoc.y;
                bestMatch[2] = resizedTemplate.cols();
                bestMatch[3] = resizedTemplate.rows();
                bestScale = scale;
            }
        }

        // 匹配度低于此值
        if (bestVal < matchScore) {
            System.out.println("bestVal = " + bestVal);
            return new double[]{-1};
        }

        double[] finalResult = new double[6];
        System.arraycopy(bestMatch, 0, finalResult, 0, 4);
        finalResult[4] = bestScale;
        finalResult[5] = bestVal;
        return finalResult;
    }

}
