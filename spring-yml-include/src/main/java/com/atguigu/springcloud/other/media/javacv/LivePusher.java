package com.atguigu.springcloud.other.media.javacv;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;

public class LivePusher {

    public void pushLocalVideo2RTMP(String localVideoPath, String rtmpAddress) throws Exception {
        FFmpegLogCallback.set();

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(localVideoPath);
        grabber.setOption("nobuffer", "1");
        grabber.start();

        // 卡顿的问题主要是采集本地视频流和推流消耗的时间大于当前采集到的视频的时长，通俗描述是 1 分钟时长的音视频数据，
        // 使用 FFmpegFrameGrabber 采集 + FFmpegFrameRecorder 推流录制，需要消耗的时间超过 1 分钟。
        // 因此可以适当的调用 setImageWidth 和 setImageHeight 降低视频的分辨率；
        // 或者是调用 setVideoBitrate 或 setVideoQuality 降低视频比特率或质量等；当然，网络也会造成卡顿。

        /*
         * 测试时推了一个剪映重新生成的高分辨率视频，其分辨率达到了 3840x2160；
         * 导致下面的推送速度跟不上实际消耗的速度；将会造成直播卡顿; 因此，可以重置其分辨率
         */
        int imageWidth = grabber.getImageWidth();
        int imageHeight = grabber.getImageHeight();
        if (imageWidth > 1920 || imageHeight > 1080) {
            double wScale = imageWidth * 1.0 / 1920;
            double hScale = imageHeight * 1.0 / 1080;
            double scale = Math.max(wScale, hScale);
            grabber.setImageWidth((int) (imageWidth / scale));
            grabber.setImageHeight((int) (imageHeight / scale));
        }

        if (grabber.getFormatContext() == null || grabber.getFormatContext().nb_streams() < 1) {
            throw new RuntimeException("本地视频中没有流数据");
        }

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                rtmpAddress, grabber.getImageWidth(), grabber.getImageHeight()
        );
        recorder.setFrameRate(grabber.getFrameRate());  // 设置帧率
        recorder.setGopSize((int) (grabber.getFrameRate() * 2));  // 设置关键帧
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);   // 视频编码格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // 视频源数据yuv
        recorder.setFormat("flv");
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC); // 音频编码格式
//        recorder.setAudioBitrate(grabber.getAudioBitrate());
        recorder.setAudioChannels(grabber.getAudioChannels());
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.start();

        // 假设 1 秒中有 30 帧数据，那么两帧数据之间的时间间隔就是 1000 / 30；
        long interval = 1_000 / (int) grabber.getFrameRate();

        long startTime = System.currentTimeMillis();

        Frame frame;
        while ((frame = grabber.grab()) != null) {
            recorder.record(frame);
            long currentTime = 1_000 * (System.currentTimeMillis() - startTime);
            long frameTime = frame.timestamp;
            long sleepTime = (frameTime - currentTime) / 1_000;
            System.out.println("推流测试 >>>>>>>> " + getFrameTime(currentTime) + " >>>>>>>> " + getFrameTime(frameTime));
            try {
                // 如果 10 分钟长的视频，只需要 5 分钟就可以推送完成又会怎么样？测试过程使用 ffplay 进行播放，发现其会跳进度，
                // 即每次中间提前推送了的时长的数据直接被跳过，还有就是推流进程结束之后，还能够继续播放几十秒钟；
                // 因此，需要使用线程休眠尽量的保持推送时间和视频时间的同步和一致；
                if (interval > 0 && sleepTime > 500 + interval) {
                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        recorder.close();
        grabber.close();
    }

    private String getFrameTime(long frameTime) {
        long mills = (frameTime / 1000) % 1000;
        int sec = (int) (frameTime / 1_000 / 1_000);
        int min = sec / 60;
        sec %= 60;
        if (min >= 60) {
            int hour = min / 60;
            min %= 60;
            return String.format("%02d:%02d:%02d.%03d", hour, min, sec, mills);
        } else {
            return String.format("%02d:%02d.%03d", min, sec, mills);
        }
    }

}
