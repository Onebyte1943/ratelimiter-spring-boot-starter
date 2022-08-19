package com.open.onebyte.ratelimiter.model.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author yangqk
 */
public enum AlgorithmEnum {
    /**
     * 固定窗口算法（计数器算法）Fixed window Algorithm
     */
    FIXED_WINDOW("fixed_window", "固定窗口算法"),
    /**
     * 滑动窗口算法 Sliding window Algorithm
     */
    SLIDING_WINDOW("sliding_window", "滑动窗口算法"),
    /**
     * 滑动窗口日志算法 Sliding window log Algorithm
     */
    SLIDING_WINDOW_LOG("sliding_window_log", "滑动窗口日志算法"),
    /**
     * 多级滑动窗口日志算法 Sliding window log Algorithm
     */
    SLIDING_WINDOW_LOG_CASCADE("sliding_window_log_cascade", "多级滑动窗口日志算法"),
    /**
     * 令牌桶算法 Token bucket Algorithm
     */
    TOKEN_BUCKET("token_bucket", "令牌桶算法"),
    /**
     * 漏桶算法 Leaky bucket Algorithm
     */
    LEAKY_BUCKET("leaky_bucket", "漏桶算法");

    private final String code;
    private final String desc;

    AlgorithmEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AlgorithmEnum getAlgorithmEnum(String algorithmName) {
        return Arrays.stream(values()).filter((algorithmEnum) -> algorithmEnum.code.equals(algorithmName)).findFirst().orElse(null);
    }

    public static boolean isSupportAlgorithm(String algorithmName) {
        AlgorithmEnum algorithmEnum = getAlgorithmEnum(algorithmName);
        return Objects.nonNull(algorithmEnum);
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
