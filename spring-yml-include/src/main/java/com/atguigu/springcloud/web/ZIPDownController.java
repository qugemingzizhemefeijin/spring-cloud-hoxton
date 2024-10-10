package com.atguigu.springcloud.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Controller
@RequestMapping(value = "/down" )
public class ZIPDownController {

    @GetMapping(value = "load" )
    public void download(HttpServletResponse response) throws Exception {
        // 获取输出流和 zip 包输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 获取当前项目的路径
        String property = System.getProperty("user.dir" );
        // 压缩包的名称
        String root = "result";
        // 处理需要处理的文件路径，并填充文件的名称
        zip.putNextEntry(new ZipEntry(root + "/data/README.md" ));
        IOUtils.write(FileUtil.readBytes(new File(property + "/README.md" )), zip);
        zip.flush();
        zip.closeEntry();

        zip.putNextEntry(new ZipEntry(root + "/data1/README.md" ));
        IOUtils.write(FileUtil.readBytes(new File(property + "/README.md" )), zip);
        zip.flush();
        zip.closeEntry();

        zip.putNextEntry(new ZipEntry(root + "/data2/README.md" ));
        IOUtils.write(FileUtil.readBytes(new File(property + "/README.md" )), zip);
        zip.flush();
        zip.closeEntry();

        zip.putNextEntry(new ZipEntry(root + "/data2/dt/README.md" ));
        IOUtils.write(FileUtil.readBytes(new File(property + "/README.md" )), zip);
        zip.flush();
        zip.closeEntry();
        // 生成输出流
        IOUtils.closeQuietly(zip);
        downloadZIP(response, outputStream.toByteArray());
    }

    private void downloadZIP(HttpServletResponse response, byte[] data) throws IOException {
        // 生成压缩文件的名称
        String idStr = DateUtil.format(new Date(), "HHmmss" );
        String format = StrUtil.format("attachment; filename=result{}.zip", idStr);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*" );
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition" );
        response.setHeader("Content-Disposition", format);
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8" );
        IOUtils.write(data, response.getOutputStream());
    }

}
