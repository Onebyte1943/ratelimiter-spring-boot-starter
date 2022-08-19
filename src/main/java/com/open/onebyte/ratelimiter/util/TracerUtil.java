package com.open.onebyte.ratelimiter.util;

import org.slf4j.MDC;

/**
 * TracerUtil
 *
 * @author yangqk
 */
public class TracerUtil {
    private static final String TRACE_ID = "traceId";

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
}
