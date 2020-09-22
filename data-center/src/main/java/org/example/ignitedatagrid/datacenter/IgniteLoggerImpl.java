package org.example.ignitedatagrid.datacenter;

import org.apache.ignite.IgniteLogger;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IgniteLoggerImpl implements IgniteLogger {

    protected final Logger internalLogger;

    public IgniteLoggerImpl(Logger logger) {
        internalLogger = logger;
    }

    public IgniteLoggerImpl(Object ctgr) {
        if (ctgr instanceof String) {
            internalLogger = LoggerFactory.getLogger((String) ctgr);
        } else {
            internalLogger = LoggerFactory.getLogger(ctgr.getClass());
        }
    }

    @Override
    public IgniteLogger getLogger(Object ctgr) {
        return new IgniteLoggerImpl(ctgr);
    }


    @Override
    public void trace(String msg) {
        internalLogger.trace(msg);
    }


    @Override
    public void debug(String msg) {
        internalLogger.debug(msg);
    }


    @Override
    public void info(String msg) {
        internalLogger.info(msg);
    }


    @Override
    public void warning(String msg, @Nullable Throwable e) {
        internalLogger.warn(msg);
    }


    @Override
    public void error(String msg, @Nullable Throwable e) {
        internalLogger.error(msg, e);
    }


    @Override
    public boolean isTraceEnabled() {
        return internalLogger.isTraceEnabled();
    }


    @Override
    public boolean isDebugEnabled() {
        return internalLogger.isDebugEnabled();
    }


    @Override
    public boolean isInfoEnabled() {
        return internalLogger.isInfoEnabled();
    }


    @Override
    public boolean isQuiet() {
        return false;
    }


    @Override
    public String fileName() {
        return null;
    }
}
