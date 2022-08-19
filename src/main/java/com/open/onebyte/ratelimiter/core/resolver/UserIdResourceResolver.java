package com.open.onebyte.ratelimiter.core.resolver;

import com.open.onebyte.ratelimiter.annotation.ResolverType;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;
import com.yuntai.udb.auth.sdk.boot.domain.util.UdbUserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * @author yangqk
 */
@Component
@ResolverType(limitType = LimitTypeEnum.USER)
public class UserIdResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(UserIdResourceResolver.class);
    /**
     * Udb UdbUserUtil class name
     */
    private static final String CLASS_NAME = "com.yuntai.udb.auth.sdk.boot.domain.util.UdbUserUtil";

    @Override
    public String resolve(String originalResource, ProceedingJoinPoint pjp) {
        String usId;
        boolean present = ClassUtils.isPresent(CLASS_NAME, ClassUtils.getDefaultClassLoader());
        if (!present) {
            logger.warn("The current environment cannot obtain user id information, if you don't need to pay attention to user information, you can ignore it.");
            return null;
        }
        try {
            usId = UdbUserUtil.getUsId().toString();
        } catch (Exception e) {
            throw new ExecuteException("The current environment cannot obtain user id information, Please check udb configuration.");
        }
        logger.info("[UserIdResourceResolver.resolve response: {}, originalResource is: {}]", usId, originalResource);
        return usId;
    }
}
