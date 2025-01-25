package com.atguigu.springcloud.other.network.nmap;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.atguigu.springcloud.utils.JsonUtil;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.nmap4j.Nmap4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Nmap4jTest {

    private static final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        List<String> ports = Lists.newArrayList("80", "443", "22");

        List<NmapPortInfo> list = null;
        String system = System.getProperty("os.name").toLowerCase();
        if (system.contains("windows")) {
            list = windowsQuery(ip, ports);
        } else {
            list = linuxQuery(ip, ports);
        }

        System.out.println(JsonUtil.toJson(list));

        threadPoolExecutor.shutdown();
    }

    /**
     * 使用 nmap4j 工具进行扫描, windows系统
     *
     * @param ip    目标 ip
     * @param ports 目标端口
     * @return 端口信息列表
     */
    public static List<NmapPortInfo> windowsQuery(String ip, List<String> ports) {
        ArrayList<NmapPortInfo> portInfos = new ArrayList<>();
        // 1.拼接端口
        String portStr = StrUtil.join(",", ports);

        //2. 指定 nmap 路径
        String path = "D:/Program Files (x86)/Nmap";
        String fileName = "temp_result.xml";


        // 3.获取当前系统信息
        String system = System.getProperty("os.name");
        log.info("当前系统：{}", system);

        Nmap4j nmap4j = new Nmap4j(path);

        //3. 读取端口耗时较长，可以使用异步
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            nmap4j.addFlags("-sV -p " + portStr + " -T5 -O -oX " + fileName);
            nmap4j.includeHosts(ip);
            try {
                nmap4j.execute();

//                NMapRun nmapRun = nmap4j.getResult();

                // 打印扫描结果
//                System.out.println(nmapRun.getHosts().get(0).getHostnames().toString());
//                System.out.println("Port 80 status: " + nmapRun.getHosts().get(0).getPorts().getPorts().get(0).getState());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, threadPoolExecutor);

        future.join();

        //4. 获取端口信息
        return getPortInfo(portInfos, fileName);
    }

    /**
     * 使用 nmap4j 工具进行扫描, linux系统
     *
     * @param ip    目标 ip
     * @param ports 目标端口
     * @return 端口信息列表
     */
    @SneakyThrows
    public static List<NmapPortInfo> linuxQuery(String ip, List<String> ports) {
        ArrayList<NmapPortInfo> portInfos = new ArrayList<>();
        // 1.拼接端口
        String portStr = StrUtil.join(",", ports);
        String fileName = "temp_result.xml";
        //2. linux namp 命令
        String nmapCommand = "nmap -sV -p " + portStr + " -T5 -O -oX " + fileName + " " + ip;

        //3. 读取端口耗时较长，可以使用异步
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Process nampProcess = null;
            try {
                // 3. 运行命令
                nampProcess = Runtime.getRuntime().exec(nmapCommand);
                // 4. 等待命令执行完成
                nampProcess.waitFor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, threadPoolExecutor);

        future.join();

        // 5. 获取端口信息
        return getPortInfo(portInfos, fileName);
    }

    @SneakyThrows
    private static List<NmapPortInfo> getPortInfo(List<NmapPortInfo> portInfos, String fileName) {
        // 获取项目所在路径
        String projectPath = System.getProperty("user.dir");
        // 拼接文件路径
        String filePath = projectPath + FileUtil.FILE_SEPARATOR + fileName;
        log.info("文件路径：{}", filePath);

        // nmap 返回 xml 格式固定，使用 dom4j 解析
        SAXReader reader = new SAXReader();
        Document document = reader.read(FileUtil.file(filePath));
        Element rootElement = document.getRootElement();
        Element hostElement = rootElement.element("host");

        Element portsElement = hostElement.element("ports");

        List<Element> portElements = portsElement.elements("port");
        for (Element port : portElements) {
            Element serviceElement = port.element("service");
            String product = serviceElement.attributeValue("product");
            String version = serviceElement.attributeValue("version");
            NmapPortInfo nmapPortInfo = new NmapPortInfo(product, version);
            portInfos.add(nmapPortInfo);
        }

        // 删除临时文件
        FileUtil.del(filePath);
        return portInfos;
    }

}
