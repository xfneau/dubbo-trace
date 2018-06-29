package com.edianzu.mall.trace.service;

import com.edianzu.mall.trace.TraceContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author xfneau
 * @description:
 * @Date Created in 16:16 2018/4/10
 */
public final class TraceContext implements AutoCloseable {

    Logger log = LoggerFactory.getLogger(TraceContext.class);

    private static ThreadLocal<TraceContent> traces = new InheritableThreadLocal<>();

    private static TraceContext context ;

    private TraceContext() {

    }

    /**
     * context init
     *
     */
    public void init() {
        String traceId = generatorTraceId();
        TraceContent content = new TraceContent(traceId);
        traces.set(new TraceContent(traceId));
        MDC.put(TraceContent.TRACE_ID, traceId);
        MDC.put(TraceContent.STEP, String.valueOf(content.getRefCnt()));
    }

    public void setTraceContent(TraceContent content) {
        traces.set(content);
    }

    public void setMDC() {
        TraceContent content = traces.get();
        if (content != null) {
            MDC.put(TraceContent.TRACE_ID, content.getTraceId());
            MDC.put(TraceContent.STEP, content.refCnt());
        }
    }


    /**
     * the id of trace
     *
     */
    public String retainTrace() {
        TraceContent trace = traces.get();
        if (trace == null) {
            return generatorTraceId();
        }
        return trace.getTraceId();
    }

    /**
     * context destroy
     */
    public void destroy() {
        if (traces != null && traces.get() != null) {
            TraceContent content = traces.get();
            double s = (System.currentTimeMillis() - content.getTime())*1.0/1000;
            traces.remove();
        }
        MDC.clear();
    }

    private String generatorTraceId() {
        return UUID.randomUUID().toString();
    }

    public static TraceContent get() {
        return traces.get();
    }

    public static TraceContext getInstance() {
        if (context == null) {
            synchronized (TraceContext.class) {
                if (context == null) {
                    context = new TraceContext();
                }
            }
        }
        return context;
    }

    @Override
    public void close() throws RuntimeException {
        destroy();
    }

}
