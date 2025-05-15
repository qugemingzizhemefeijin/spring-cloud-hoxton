package com.atguigu.springcloud.other.media.m3u8;

import com.atguigu.springcloud.utils.http.HttpUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadKwai {

    public static void main(String[] args) throws Exception {
        downLoadVideo("C:\\Users\\dell\\Desktop\\index2.m3u8");
    }

    private static void downLoadVideo(String m3u8Path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(m3u8Path))
                .stream()
                .filter(s -> s.startsWith("http"))
                .collect(Collectors.toList());

        String dir = "E:/222";
        StringBuilder buf = new StringBuilder("ffmpeg -i \"concat:");
        for (int i = 0, size = lines.size(); i < size; i++) {
            String url = lines.get(i);

            String name = url.substring(url.lastIndexOf("/")+1);

            //System.out.println("i = " + getPadding(i, 4) + ", url = " + url);

            String fileName = getPadding(i, 4) + "-" + name;
            String filePath = dir + "/" + fileName;
            HttpUtils.downloadFile(url, filePath);

            buf.append(fileName);

            if (i < size - 1) {
                buf.append("|");
            }
        }
        buf.append("\" -c copy -bsf:a aac_adtstoasc -movflags +faststart output.mp4");

        writeBatFile(dir, buf);

        // 检查是否完成下载
        for (int i = 0, size = lines.size(); i < size; i++) {
            String url = lines.get(i);
            String name = url.substring(url.lastIndexOf("/")+1);
            String fileName = getPadding(i, 4) + "-" + name;
            String filePath = dir + "/" + fileName;

            if (!checkFile(filePath)) {
                System.out.println(filePath);
            }
        }
    }

    private static boolean checkFile(String filePath) {
        return new File(filePath).exists();
    }

    private static void writeBatFile(String dir, StringBuilder buf) throws IOException {
        File file = new File(dir + "/go.sh");
        try (FileWriter fw = new FileWriter(file); PrintWriter pw = new PrintWriter(fw);) {
            pw.print(buf.toString());
            pw.flush();
        }
    }

    private static String getPadding(int i, int padding) {
        return String.format("%" + padding + "d", i).replace(" ", "0");
    }

}
