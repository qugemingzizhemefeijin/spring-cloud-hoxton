package com.atguigu.springcloud.other.pictures.smartjavaai;

import cn.smartjavaai.common.entity.DetectionResponse;
import cn.smartjavaai.face.config.FaceModelConfig;
import cn.smartjavaai.face.enums.FaceModelEnum;
import cn.smartjavaai.face.factory.FaceModelFactory;
import cn.smartjavaai.face.model.facerec.FaceModel;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

// git@gitee.com:dengwenjie/SmartJavaAI.git
// https://blog.csdn.net/deng775747758/article/details/147859883 Java 实现静默活体检测完整教程
// https://juejin.cn/post/7502773930349117481 用 Java 搞定人脸属性检测：年龄、性别、口罩，眼睛，姿态
@Slf4j
public class SeetaFace6Demo {

    /**
     * 人脸检测(自定义模型参数)
     * 图片参数：图片路径
     */
    public void testFaceDetectCustomConfig(){
        FaceModelConfig config = new FaceModelConfig();
        config.setModelEnum(FaceModelEnum.SEETA_FACE6_MODEL);//人脸模型
        config.setModelPath("E:\\Downloads\\sf3.0_models");
        FaceModel faceModel = FaceModelFactory.getInstance().getModel(config);
        DetectionResponse detectedResult = faceModel.detect("C:\\Users\\a\\Desktop\\jsy.jpg");
        log.info("人脸检测结果：{}", JSONObject.toJSONString(detectedResult));
    }

    /**
     * 人脸检测并绘制人脸框
     */
    public void testFaceDetectAndDraw(){
        FaceModelConfig config = new FaceModelConfig();
        config.setModelEnum(FaceModelEnum.SEETA_FACE6_MODEL);//人脸模型
        config.setModelPath("E:\\Downloads\\sf3.0_models");
        FaceModel faceModel = FaceModelFactory.getInstance().getModel(config);
        faceModel.detectAndDraw("C:\\Users\\a\\Desktop\\jsy.jpg","C:\\Users\\a\\Desktop\\jsy_detected.jpg");
    }

    public static void main(String[] args) {
        SeetaFace6Demo demo = new SeetaFace6Demo();
        demo.testFaceDetectAndDraw();
    }

}
