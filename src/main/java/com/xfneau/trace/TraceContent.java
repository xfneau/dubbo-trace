package com.xfneau.trace;

import lombok.ToString;

/**
 * @author xfneau
 * @description:
 * @Date Created in 16:27 2018/4/10
 */
@ToString
public class TraceContent {

    private String refCnt;

    private String traceId;

    private long time;

    public static final String TRACE_ID = "traceId";

    public static final String STEP = "step";

    public static final String LOG_FORMAT = "traceId:{} step:{} 调用耗时:{}s";

    public TraceContent(String traceId) {
        this.time = System.currentTimeMillis();
        this.traceId = traceId;
        this.refCnt = "1";
    }

    public String refCnt() {
        return refCnt;
    }

    public void setRefCnt(String refCnt) {
        this.refCnt = refCnt;
    }

    /**
     * 1 -> 1.1
     */
    public String nextStep() {
        return refCnt + ".1";
    }

    /**
     * 1.1 -> 1.2
     */
    public String currentNextStep() {
        if (refCnt.lastIndexOf(".") < refCnt.length()) {
            int lastIndex = refCnt.lastIndexOf(".");
            int n = Integer.valueOf(refCnt.substring(lastIndex + 1)) + 1;
            refCnt = refCnt.substring(0, lastIndex + 1) + n;
        }
        return refCnt;
    }


    public String getTraceId() {
        return traceId;
    }

    public long getTime() {
        return time;
    }

    public String getRefCnt() {
        return refCnt;
    }
}
