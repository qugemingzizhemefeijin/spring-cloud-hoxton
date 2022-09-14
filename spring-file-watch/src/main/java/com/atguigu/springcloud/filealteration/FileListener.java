package com.atguigu.springcloud.filealteration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

@Slf4j
public class FileListener extends FileAlterationListenerAdaptor {

    /**
     * File system observer started checking event.
     *
     * @param observer The file system observer (ignored)
     */
    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    /**
     * Directory created Event.
     *
     * @param directory The directory created (ignored)
     */
    @Override
    public void onDirectoryCreate(File directory) {
        log.info("[Deleted Directory] : {}", directory.getAbsolutePath());
    }

    /**
     * Directory changed Event.
     *
     * @param directory The directory changed (ignored)
     */
    @Override
    public void onDirectoryChange(File directory) {
        log.info("[Changed Directory] : {}", directory.getAbsolutePath());
    }

    /**
     * Directory deleted Event.
     *
     * @param directory The directory deleted (ignored)
     */
    @Override
    public void onDirectoryDelete(File directory) {
        log.info("[Created Directory] : {}", directory.getAbsolutePath());
    }

    /**
     * File created Event.
     *
     * @param file The file created (ignored)
     */
    @Override
    public void onFileCreate(File file) {
        log.info("[Created File] : {}", file.getAbsolutePath());
    }

    /**
     * File changed Event.
     *
     * @param file The file changed (ignored)
     */
    @Override
    public void onFileChange(File file) {
        log.info("[Amended File] : {}", file.getAbsolutePath());
    }

    /**
     * File deleted Event.
     *
     * @param file The file deleted (ignored)
     */
    @Override
    public void onFileDelete(File file) {
        log.info("[Deleted File] : {}", file.getAbsolutePath());
    }

    /**
     * File system observer finished checking event.
     *
     * @param observer The file system observer (ignored)
     */
    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }

}
