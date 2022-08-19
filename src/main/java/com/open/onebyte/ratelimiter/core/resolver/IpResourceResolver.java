package com.open.onebyte.ratelimiter.core.resolver;

import com.open.onebyte.ratelimiter.annotation.ResolverType;
import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;
import com.open.onebyte.ratelimiter.util.NetUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yangqk
 */
@Component
@ResolverType(limitType = LimitTypeEnum.IP)
public class IpResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(IpResourceResolver.class);

    @Override
    public String resolve(String originalResource, ProceedingJoinPoint pjp) {
        String address = NetUtils.getClientIpAddressByServlet();
        logger.info("[IpResourceResolver.resolve response: {}, originalResource is: {}]", address, originalResource);
        return address;
    }
}
