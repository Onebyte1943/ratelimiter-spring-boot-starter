package com.open.onebyte.ratelimiter.model.enums;

/**
 * @author yangqk
 */
public enum FallbackStrategyEnum {
    /**
     * 快速失败
     */
    FAIL_FAST,
    /**
     * 降级处理
     */
    FALLBACK
}
