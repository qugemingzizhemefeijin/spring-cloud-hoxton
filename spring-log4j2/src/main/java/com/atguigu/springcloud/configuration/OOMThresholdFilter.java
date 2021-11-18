package com.atguigu.springcloud.configuration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

@Plugin(name = "OOMThresholdFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class OOMThresholdFilter extends AbstractFilter {

    private final Level level;

    private final String exceptionClass;

    private OOMThresholdFilter(final Level level, String exceptionClass, final Result onMatch, final Result onMismatch) {
        super(onMatch, onMismatch);
        this.level = level;
        this.exceptionClass = exceptionClass;
    }

    @Override
    public Result filter(final LogEvent event) {
        Result r = filter(event.getLevel());
        if(r == onMismatch) {
            return r;
        }
        return filterException(event);
    }

    private Result filter(final Level testLevel) {
        return testLevel.isMoreSpecificThan(this.level) ? onMatch : onMismatch;
    }

    private Result filterException(final LogEvent event) {
        Throwable t = event.getThrown();
        if(exceptionClass != null && t != null) {
            if(t.getClass().getName().equals(exceptionClass)) {
                return onMatch;
            }
        }
        return onMismatch;
    }

    @PluginFactory
    public static OOMThresholdFilter createFilter(
            @PluginAttribute("level") final Level level,
            @PluginAttribute("exceptionClass") final String exceptionClass,
            @PluginAttribute("onMatch") final Result match,
            @PluginAttribute("onMismatch") final Result mismatch) {
        final String actualExceptionClass = exceptionClass == null ? "NONE" : (exceptionClass.trim().equals("") ? "NONE" : exceptionClass.trim());
        final Level actualLevel = level == null ? Level.ERROR : level;
        final Result onMatch = match == null ? Result.NEUTRAL : match;
        final Result onMismatch = mismatch == null ? Result.DENY : mismatch;
        return new OOMThresholdFilter(actualLevel, actualExceptionClass, onMatch, onMismatch);
    }

}
