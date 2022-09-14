package com.atguigu.springcloud.jnotiy;

import lombok.extern.slf4j.Slf4j;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class CustomJnotifyAdapter extends JNotifyAdapter {

    //关注目录的事件
    public static final int MASK = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;

    /**
     * jnotify动态库 - 32位
     */
    public static final String NATIVE_LIBRARIES_32BIT = "/spring-file-watch/lib/32bits/";

    /**
     * jnotify动态库 - 64位
     */
    public static final String NATIVE_LIBRARIES_64BIT = "/spring-file-watch/lib/64bits/";

    private final String rootDir;

    public CustomJnotifyAdapter(String rootDir){
        this.rootDir = rootDir;
    }

    /**
     * 容器启动时启动监视程序
     */
    public void beginWatch() throws Exception {

        log.debug("-----------Jnotify test ---------");

        Properties sysProps = System.getProperties();
        String osArch = (String) sysProps.get("os.arch");
        String osName = (String) sysProps.get("os.name");
        String userDir = (String) sysProps.getProperty("user.dir");
        log.debug("os.arch: " + osArch);
        log.debug("os.name: " + osName);
        log.debug("userDir: " + userDir);
        log.debug("java.class.path: " + sysProps.get("java.class.path"));

        // 直接调用Jnotify时， 会发生异常：java.lang.UnsatisfiedLinkError: no jnotify_64bit in java.library.path
        // 这是由于Jnotify使用JNI技术来加载dll文件，如果在类路径下没有发现相应的文件，就会抛出此异常。
        // 因此可以通过指定程序的启动参数: java -Djava.library.path=/path/to/dll，
        // 或者是通过修改JVM运行时的系统变量的方式来指定dll文件的路径，如下：

        // 判断系统是32bit还是64bit，决定调用对应的dll文件
        String jnotifyDir = NATIVE_LIBRARIES_64BIT;
        if (!osArch.contains("64")) {
            jnotifyDir = NATIVE_LIBRARIES_32BIT;
        }
        log.debug("jnotifyDir: " + jnotifyDir);
        // 获取目录路径
        String pathToAdd = userDir + jnotifyDir ;
        boolean isAdded = false;
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);
        final String[] paths = (String[]) usrPathsField.get(null);
        log.debug("usr_paths: " + Arrays.toString(paths));
        for (String path : paths) {
            if (path.equals(pathToAdd)) {
                isAdded  = true;
                break;
            }
        }
        if (!isAdded) {
            final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
            newPaths[newPaths.length - 1] = pathToAdd;
            usrPathsField.set(null, newPaths);
        }

        log.debug("java.library.path: " + System.getProperty("java.library.path"));
        log.debug("usr_paths: " + Arrays.toString((String[]) usrPathsField.get(null)));
        usrPathsField.setAccessible(false);
        log.debug("类路径加载完成");


        //是否监视子目录，即级联监视
        boolean watchSubtree = true;
        //监听程序Id
        int watchId;
        //添加到监视队列中
        try {
            watchId = JNotify.addWatch(rootDir, MASK, watchSubtree, this);
            log.info("jnotify -----------启动成功-----------, watchId = {}",watchId);
        } catch (JNotifyException e) {
            e.printStackTrace();
        }
        // 死循环，线程一直执行，休眠一分钟后继续执行，主要是为了让主线程一直执行
        // 休眠时间和监测文件发生的效率无关（就是说不是监视目录文件改变一分钟后才监测到，监测几乎是实时的，调用本地系统库）
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {// ignore it
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void fileCreated(int wd, String rootPath, String name) {
        log.info("文件被创建, 创建位置为： " + rootPath + "/" + name);
    }

    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        log.info("文件被删除, 被删除的文件名为：" + rootPath + name);
    }

    @Override
    public void fileModified(int wd, String rootPath, String name) {
        log.info("文件内容被修改, 文件名为：" + rootPath + "/" + name);
    }

    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        log.info("文件被重命名, 原文件名为：" + rootPath + "/" + oldName
                + ", 现文件名为：" + rootPath + "/" + newName);

    }

    private static String getBaseDir() {
        try {
            return new ClassPathResource("").getFile().getAbsolutePath();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
