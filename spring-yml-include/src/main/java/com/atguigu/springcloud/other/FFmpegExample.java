package com.atguigu.springcloud.other;

import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpegExample {

    // 经过测试以下格式均可转码
    // .mp4;.asf;.avi;.dat;.f4v;.flv;.mkv;.mov;.mpg;.rmvb;.ts;.vob;.webm;.wmv;.vob
    public boolean example(FileConvertInfo fileConvertInfo) throws IOException {
        // ffmpeg -i inputpath -c:v libx264 -crf 19 -strict experimental outputpath
        ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(fileConvertInfo.getFilePath());
        ffmpeg.addArgument("-c:v");
        ffmpeg.addArgument("libx264");
        ffmpeg.addArgument("-crf");
        ffmpeg.addArgument("19");
        ffmpeg.addArgument("-strict");
        ffmpeg.addArgument("experimental");
        ffmpeg.addArgument(fileConvertInfo.getFileDirPath() + "convert.mp4");
        ffmpeg.execute();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ffmpeg.getErrorStream()))) {
            blockFfmpeg(br);
        }
        File file = new File(fileConvertInfo.getFileDirPath() + "finish.txt");
        return file.createNewFile();
    }

    private static void blockFfmpeg(BufferedReader br) throws IOException {
        String line;
        // 该方法阻塞线程，直至合成成功
        while ((line = br.readLine()) != null) {
            doNothing(line);
        }
    }

    private static void doNothing(String line) {
        System.out.println(line);
    }

}
