package com.atguigu.springcloud.other.io.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Slf4j
public class SftpUtils {

    /**
     * 连接 sftp 服务器，创建 channel 和 session
     *
     * @param host     IP
     * @param port     端口
     * @param user     登录名
     * @param password 登录密码
     * @return FtpJchClient
     */
    public static FtpJchClient getConnect(String host, int port, String user, String password) throws Exception {
        log.info("ftpJSch host = {} port = {} user = {}", host, port , user);
        JSch jsch = new JSch();

        Session sshSession = jsch.getSession(user, host, port);
        if(StringUtils.isNotBlank(password)) {
            // 添加密码
            sshSession.setPassword(password);
        } else {
            //添加私钥
            jsch.addIdentity("/home/"+user+"/.ssh/id_rsa");
        }
        Properties sshConfig = new Properties();
        // 严格主机密钥检查
        sshConfig.put("StrictHostKeyChecking", "no");

        sshSession.setConfig(sshConfig);
        // 开启sshSession链接
        sshSession.connect();
        // 获取sftp通道
        Channel channel = sshSession.openChannel("sftp");
        // 开启
        channel.connect();
        ChannelSftp sftp = (ChannelSftp) channel;

        return new FtpJchClient(sshSession, sftp);
    }

    /**
     * 关闭连接
     * @param client FtpJchClient
     */
    public static void close(FtpJchClient client) throws Exception {
        client.close();
    }

}
