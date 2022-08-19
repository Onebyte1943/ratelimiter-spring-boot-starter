package com.open.onebyte.ratelimiter.core.resolver;

import com.open.onebyte.ratelimiter.annotation.ResolverType;
import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;
import com.open.onebyte.ratelimiter.support.AbstractRateLimiterSupport;
import com.open.onebyte.ratelimiter.util.MethodUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yangqk
 */
@Component
@ResolverType(limitType = LimitTypeEnum.DEFAULT)
public class DefaultResourceResolver extends AbstractRateLimiterSupport implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(DefaultResourceResolver.class);

    @Override
    public String resolve(String originalResource, ProceedingJoinPoint pjp) {
        if (StringUtils.isBlank(originalResource)) {
            Method method = resolveMethod(pjp);
            originalResource = MethodUtil.resolveMethodName(method);
        }
        logger.info("[DefaultResourceResolver.resolve response: {}]", originalResource);
        return originalResource;
    }
}
