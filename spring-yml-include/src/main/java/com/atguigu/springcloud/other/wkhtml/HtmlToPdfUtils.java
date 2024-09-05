package com.atguigu.springcloud.other.wkhtml;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// wkhtmltopdf 下载地址 https://wkhtmltopdf.org/downloads.html
@Slf4j
public class HtmlToPdfUtils {

    private static final String BASE_PATH = "E:\\mygithub\\spring-cloud-hoxton\\spring-yml-include\\src\\main\\java\\com\\atguigu\\springcloud\\other\\wkhtml\\";

    /**
     * wkhtmltopdf在 Windows、Linux系统中的安装路径
     */
    private static final String WINDOWS_HTML_TO_PDF_TOOL = "D:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";
    private static final String LINUX_HTML_TO_PDF_TOOL = "/opt/wkhtmltox/bin/wkhtmltopdf";

    private static final String toImgTool = "D:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltoimage.exe";

    /**
     * 页眉图片html：Windows、Linux系统中的路径
     */
    private static final String WINDOWS_HEAD_HTML = BASE_PATH + "head.html";
    private static final String LINUX_HEAD_HTML = "/opt/head.html";

    /**
     * 页脚图片html：Windows、Linux系统中的路径
     */
    private static final String WINDOWS_FOOT_HTML = BASE_PATH + "foot.html";
    private static final String LINUX_FOOT_HTML = "/opt/foot.html";

    /**
     * 临时文件存放目录：Windows、Linux系统中的路径
     */
    public static final String WINDOWS_FILE_URL = "E:\\file";
    public static final String LINUX_FILE_URL = "/opt/temporary/file";

    static {
        String fileUrl = isWindowsSystem() ? WINDOWS_FILE_URL : LINUX_FILE_URL;
        File f = new File(fileUrl);
        if(!f.exists()){
            f.mkdirs();
        }
    }

    /**
     * html字符串转pdf，会生成垃圾文件，需要定时清理
     *
     * @param data              替换好的html字符串
     * @param destFileName      保存pdf的名称
     * @return 返回pdf成功生成的路径
     */
    public static String convertStringToHtml(String data, String destFileName) {
        return convert(stringToHtml(data), destFileName);
    }

    /**
     * 判断当前系统是否是Windows系统
     * @return true：Windows系统，false：Linux系统
     */
    public static boolean isWindowsSystem(){
        String property = System.getProperty("os.name").toLowerCase();
        return property.contains("windows");
    }

    /**
     * html转pdf（加页眉页脚）
     *
     * @param srcPath       html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destFileName 保存pdf的名称
     * @return 返回pdf成功生成的路径
     */
    public static String convert(String srcPath, String destFileName) {
        destFileName = (isWindowsSystem() ? WINDOWS_FILE_URL + "\\" : LINUX_FILE_URL + "/")  + destFileName;

        File file = new File(destFileName);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }

        StringBuilder cmd = new StringBuilder();
        String toPdfTool = isWindowsSystem() ? WINDOWS_HTML_TO_PDF_TOOL : LINUX_HTML_TO_PDF_TOOL;

        // 这里可以拼接页眉页脚等参数
        cmd.append(toPdfTool);
        cmd.append(" ");
        // wkhtmltopdf默认不允许访问本地文件，需加入以下参数
        cmd.append("  --enable-local-file-access");
//        cmd.append("  --page-size A4");
        cmd.append(" ");
        cmd.append("  --disable-smart-shrinking");
        // 页眉图片
        cmd.append(" --header-html " + (isWindowsSystem() ? WINDOWS_HEAD_HTML : LINUX_HEAD_HTML));
        cmd.append(" --header-spacing 5");
        // 页脚图片
        cmd.append("  --footer-html " + (isWindowsSystem() ? WINDOWS_FOOT_HTML : LINUX_FOOT_HTML));
        cmd.append("  " + srcPath);
        cmd.append(" ");
        cmd.append(destFileName);

        try {
            log.info("html转pdf命令：{}", cmd.toString());
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
            log.info("html转pdf成功：html路径：{} pdf保存路径：{}", srcPath, destFileName);
        } catch (Exception e) {
            destFileName = "";
            e.printStackTrace();
            log.error("html转pdf失败：html路径：{} pdf保存路径：{}", srcPath, destFileName);
        }

        return destFileName;
    }

    /**
     * html字符串转pdf，会生成临时html文件，需要定时清理
     *
     * @param data        替换好的html字符串
     * @return 返回生成的临时文件名称
     */
    public static String stringToHtml(String data) {
        String srcPath = "";
        OutputStreamWriter osw = null;
        try {
            // 生成随机名字
            String code = String.format("%04d",(int) ((Math.random()*9+1) * 1000));
            String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 根据系统，创建字符输出流对象，负责向文件内写入
            if (!isWindowsSystem()) {
                // 非Windows 系统
                srcPath = LINUX_FILE_URL + "/" +String.format("%s%s", code , nowDate) + ".html";
            } else {
                srcPath = WINDOWS_FILE_URL + "\\" + String.format("%s%s", code , nowDate) + ".html";
            }

            osw = new OutputStreamWriter(new FileOutputStream(srcPath), StandardCharsets.UTF_8);

            log.info("html转pdf，生成临时文件：{}", srcPath);

            // 将str里面的内容读取到fw所指定的文件中
            osw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("html转pdf，生成临时文件失败：{}", e);
        }finally{
            if(osw!=null){
                try {
                    osw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    log.error("html转pdf，生成临时文件关流失败：{}", e);
                }
            }
        }
        return srcPath;
    }

    public static boolean htmlToImg(String srcPath, String destPath) {
        try {
            String destFileName = (isWindowsSystem() ? WINDOWS_FILE_URL + "\\" : LINUX_FILE_URL + "/")  + destPath;

            File file = new File(destFileName);
            File parent = file.getParentFile();
            //如果pdf保存路径不存在，则创建路径
            if (!parent.exists()) {
                parent.mkdirs();
            }
            StringBuilder cmd = new StringBuilder();
            if (isWindowsSystem()) {
                //非windows 系统
                //toPdfTool = "/home/ubuntu/wkhtmltox/bin/wkhtmltopdf";
            }
            cmd.append(toImgTool);
            cmd.append(" ");
            //cmd.append("--crop-w 400 --width 1680 --quality 50 ");
            cmd.append(srcPath);
            cmd.append(" ");
            cmd.append(destFileName);

            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        HtmlToPdfUtils.convert(BASE_PATH + "test.html", "wkhtmltopdf.pdf");

        HtmlToPdfUtils.htmlToImg(BASE_PATH + "test.html", "wkhtmltopdf.jpg");
    }

}
