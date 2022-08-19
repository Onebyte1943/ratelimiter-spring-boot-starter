package com.open.onebyte.ratelimiter.core.resolver;

import com.open.onebyte.ratelimiter.annotation.ResolverType;
import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;
import com.open.onebyte.ratelimiter.support.AbstractRateLimiterSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yangqk
 */
@Component
@ResolverType(limitType = LimitTypeEnum.CUSTOMIZED)
public class CustomizedResourceResolver extends AbstractRateLimiterSupport implements ResourceResolver {

    @Override
    public String resolve(String originalResource, ProceedingJoinPoint pjp) {
        Method method = resolveMethod(pjp);
        return getResourceNameCustomizedBySpEL(originalResource, method, pjp);
    }
}
