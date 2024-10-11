package com.atguigu.springcloud.other.io.sftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Slf4j
class FtpJchClient implements AutoCloseable {

    private static final String SPLIT = "/";

    private final ChannelSftp channel;
    private final Session sshSession;

    FtpJchClient(Session sshSession, ChannelSftp channel) {
        this.sshSession = sshSession;
        this.channel = channel;
    }

    /**
     * sftp 上传文件
     *
     * @param directory  上传到 sftp 的目录
     * @param uploadFile 上传 sftp 的文件
     */
    public void upload(String directory, String uploadFile) {
        log.info("上传 sftp 目录 {}, 上传 sftp 的文件 {}", directory, uploadFile);
        File file = new File(uploadFile);
        if (!FileUtil.exist(file)) {
            log.info("上传文件不存在");
            return;
        }
        try (FileInputStream input = new FileInputStream(file)) {
            // sftp 上传的路径不存在则需要创建
            if (!isDirExist(directory)) {
                createDir(directory);
            }
            // 切换到上传目录并上传文件
            channel.cd(directory);
            // 0 覆盖 1 恢复模式 2 追加模式
            channel.put(input, file.getName(), ChannelSftp.OVERWRITE);
            log.info("文件 {} 上传 sftp 成功", uploadFile);
        } catch (Exception e) {
            log.warn("文件 {} 上传 sftp 失败", uploadFile, e);
        }
    }

    /**
     * 创建目录
     *
     * @param document 目录名称
     * @return
     */
    public void createDir(String document) {
        try {
            log.info("开始创建目录 {}", document);
            String[] paths = document.split(SPLIT);
            String filePath = SPLIT;
            for (String path : paths) {
                if (StrUtil.isBlank(path)) {
                    continue;
                }
                filePath += path + SPLIT;
                if (isDirExist(filePath)) {
                    channel.cd(filePath);
                } else {
                    // 建立目录并设置为当前目录
                    channel.mkdir(filePath);
                    channel.cd(filePath);
                }
            }
            log.info("成功创建文件夹 {} ", document);
        } catch (SftpException e) {
            log.warn("sftp 目录创建失败", e);
        }
    }

    /**
     * 将输入流上传到SFTP服务器，作为文件
     *
     * @param directory 上传到SFTP服务器的路径
     * @param filename  上传到SFTP服务器后的文件名
     * @param input     输入流
     */
    public boolean upload(String directory, String filename, InputStream input) {
        try {
            if (StrUtil.isNotBlank(directory) && StrUtil.isNotBlank(filename)) {
                //如果文件夹不存在，则创建文件夹
                if (!isDirExist(directory)) {
                    createDir(directory);
                }
                //切换到指定文件夹
                channel.cd(directory);
                channel.put(input, filename);
                return true;
            }
        } catch (SftpException e) {
            log.warn("文件 {} 上传sftp失败", filename, e);
        }
        return false;
    }

    /**
     * 判断 ftp 上的目录是否存在
     */
    public boolean isDirExist(String directory) {
        try {
            SftpATTRS attrs = channel.lstat(directory);
            return attrs.isDir();
        } catch (Exception e) {
            log.warn("sftp 目录不存在 {}", directory);
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param directory 下载目录
     * @param filename  下载的文件
     * @param saveFile  存在本地的路径
     */
    public void download(String directory, String filename, String saveFile) {
        FileOutputStream outputStream = null;
        try {
            channel.cd(directory);
            File file = new File(saveFile);
            outputStream = new FileOutputStream(file);
            channel.get(filename, outputStream);
        } catch (Exception e) {
            log.warn("文件下载异常 dir {} file {} ", directory, filename, e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.warn("[SftpUtil-download] - warn :[{}]", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param directory
     * @param filename
     * @param response
     */
    public void download(String directory, String filename, HttpServletResponse response) {
        try {
            channel.cd(directory);
            InputStream inputStream = channel.get(filename);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            // 设置response的Header
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Location", filename);
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(bytes);
            toClient.flush();
        } catch (Exception e) {
            log.warn("sftp文件下载异常 {} ", e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param directory 要删除文件所在目录
     * @param filename  要删除的文件
     */
    public void delete(String directory, String filename) {
        try {

            if (StrUtil.isBlank(filename)) {
                channel.rmdir(directory);
            } else {
                channel.cd(directory);
                channel.rm(filename);
            }
        } catch (Exception e) {
            log.warn("文件删除异常 {} ", e.getMessage(), e);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    @SuppressWarnings("unchecked")
    public List<ChannelSftp.LsEntry> listFiles(String directory) {
        try {
            // channel.cd(directory);
            Vector<?> ls = channel.ls(directory);
            return (List<ChannelSftp.LsEntry>) ls;
        } catch (Exception e) {
            log.warn("列出目录 {} 下的文件异常 {}", directory, e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * 判断文件或目录是否存在
     *
     * @param path 文件或目录路径
     * @return true 存在; false不存在
     */
    public boolean isExist(String path) {
        try {
            channel.lstat(path);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }

    /**
     * 直接从ftp上获取流
     *
     * @param directory 上传文件目录
     * @return InputStream 输入流
     */
    public InputStream get(String directory) {
        try {
            return channel.get(directory);
        } catch (Exception e) {
            log.warn("sftpUtil get directory error={}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if(channel != null) {
            try {
                channel.disconnect();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if(sshSession != null) {
            try {
                sshSession.disconnect();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
