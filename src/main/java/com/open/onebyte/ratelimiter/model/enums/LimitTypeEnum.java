package com.open.onebyte.ratelimiter.model.enums;

/**
 * @author yangqk
 */
public enum LimitTypeEnum {
    /**
     * 基于ip
     */
    IP("ip", "ip地址维度"),
    /**
     * 基于用户
     */
    USER("user", "用户维度"),
    /**
     * 自定义维度
     */
    CUSTOMIZED("customized", "自定义维度"),
    /**
     * 基于接口
     */
    DEFAULT("default", "接口维度");

    private final String code;
    private final String desc;

    LimitTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LimitTypeEnum getLimitType(String limitTypeName) {
        for (LimitTypeEnum limitType : values()) {
            if (limitType.code.equals(limitTypeName)) {
                return limitType;
            }
        }
        return null;
    }

    public static boolean isCustomized(String limitType) {
        return CUSTOMIZED.getCode().equals(limitType);
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
