package com.atguigu.springcloud.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Plugin(name = "CustomAppender", category = Node.CATEGORY, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class CustomAppender extends AbstractAppender {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final boolean async;

    private final ExecutorService singleThreadPool;

    protected CustomAppender(String name, boolean async, Integer bufferSize, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.async = async;
        if(this.async) {
            int capacity = DEFAULT_BUFFER_SIZE;
            if(bufferSize != null && bufferSize > 0) {
                capacity = bufferSize;
            }

            this.singleThreadPool = new ThreadPoolExecutor(
                    1,
                    1,
                    0,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(capacity),
                    new NameableThreadFactory("async_slow_sql_", true),
                    (r, executor) -> {
                // 丢弃，并打印一句话
                log.warn("slow sql queue pool full");
            });
        } else {
            this.singleThreadPool = null;
        }
    }

    @Override
    public void append(LogEvent event) {
        final byte[] bytes = getLayout().toByteArray(event);
        String message = new String(bytes);

        getPropertyArray();

        String[] s = message.split("####");

        // 真正可以在这里异步写入数据库
        String date = s[0];
        String traceId = s[1];
        String thread = s[2];
        String sql = s[3];

        if(this.async) {
            this.singleThreadPool.execute(() -> {
                log.info("async date = {}", date);
                log.info("async traceId = {}", traceId);
                log.info("async thread = {}", thread);
                log.info("async sql = {}", sql);
            });
        } else {
            log.info("date = {}", date);
            log.info("traceId = {}", traceId);
            log.info("thread = {}", thread);
            log.info("sql = {}", sql);
        }
    }

    // 下面这个方法可以接收配置文件中的参数信息
    @PluginFactory
    public static CustomAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("async") boolean async,
            @PluginAttribute("bufferSize") Integer bufferSize,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions
    ) {
        if (name == null) {
            LOGGER.error("No name for CustomAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new CustomAppender(name, async, bufferSize, filter, layout, ignoreExceptions);
    }

}
