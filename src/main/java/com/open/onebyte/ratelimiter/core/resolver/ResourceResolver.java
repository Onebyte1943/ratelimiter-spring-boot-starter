package com.open.onebyte.ratelimiter.core.resolver;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author yangqk
 */
public interface ResourceResolver {

    /**
     * 解析 key
     *
     * @param originalResource 初始值
     * @param pjp              ProceedingJoinPoint 对象
     * @return 解析后的值
     */
    String resolve(String originalResource, ProceedingJoinPoint pjp);
}
