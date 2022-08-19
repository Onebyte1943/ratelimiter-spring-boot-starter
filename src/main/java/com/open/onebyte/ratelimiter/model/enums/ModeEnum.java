package com.open.onebyte.ratelimiter.model.enums;

/**
 * @author yangqk
 */
public enum ModeEnum {

    /**
     * 单机
     */
    STANDALONE("standalone", "单机"),
    /**
     * 集群
     */
    CLUSTER("cluster", "集群");

    private final String code;
    private final String desc;

    ModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
