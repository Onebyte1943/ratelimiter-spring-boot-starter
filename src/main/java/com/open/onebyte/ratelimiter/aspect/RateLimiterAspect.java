package com.open.onebyte.ratelimiter.aspect;

import com.open.onebyte.ratelimiter.annotation.RateLimiter;
import com.open.onebyte.ratelimiter.config.properties.RateLimiterProperties;
import com.open.onebyte.ratelimiter.core.DefaultRateLimiter;
import com.open.onebyte.ratelimiter.core.resolver.ResourceResolver;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import com.open.onebyte.ratelimiter.exception.RateLimitException;
import com.open.onebyte.ratelimiter.factory.ResolverContext;
import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;
import com.open.onebyte.ratelimiter.support.RateLimiterAspectSupport;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * The Aspect of rateLimiter.
 *
 * @author yangqk
 */
@Aspect
public class RateLimiterAspect extends RateLimiterAspectSupport {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterAspect.class);

    private final DefaultRateLimiter defaultRateLimiter;

    public RateLimiterAspect(DefaultRateLimiter defaultRateLimiter) {
        this.defaultRateLimiter = defaultRateLimiter;
    }

    @Pointcut("@annotation(com.open.onebyte.ratelimiter.annotation.RateLimiter)")
    public void rateLimiterAnnotationPointcut() {
    }


    @Around("rateLimiterAnnotationPointcut()")
    public Object invokeResource(ProceedingJoinPoint pjp) throws Throwable {
        Method originMethod = resolveMethod(pjp);

        RateLimiter rateLimiter = originMethod.getAnnotation(RateLimiter.class);
        if (rateLimiter == null) {
            // Should not go through here.
            throw new IllegalStateException("Wrong state for RateLimiter annotation");
        }

        try {
            allow(rateLimiter, pjp);
            return pjp.proceed();
        } catch (RateLimitException e) {
            // Triggers flow control.
            return handleRateLimitException(pjp, rateLimiter, e);
        } catch (ExecuteException e) {
            return handleExecuteException(pjp, e);
        } catch (Throwable ex) {
            if (ex instanceof RuntimeException) {
                throw new RuntimeException(ex);
            }
            throw ex;
        }
    }

    private void allow(RateLimiter rateLimiter, ProceedingJoinPoint pjp) {
        String[] resources = rateLimiter.resources();
        String[] instances = rateLimiter.instances();

        if (ArrayUtils.isEmpty(instances)) {
            throw new IllegalArgumentException("The instances must not be empty.");
        }

        if (ArrayUtils.isNotEmpty(resources)) {
            if (resources.length != instances.length) {
                throw new IllegalArgumentException("There is no one-to-one correspondence between resources and instances.");
            }
        }

        IntStream.range(0, instances.length).forEachOrdered(i -> {
            String originalResource = null;
            if (ArrayUtils.isNotEmpty(resources)) {
                originalResource = resources[i];
            }
            String instance = instances[i];

            String resource = this.resolve(instance, originalResource, pjp);
            // 在 ip 维度、用户 id 维度下，可能为空，此时不进行流控
            if (StringUtils.isBlank(resource)) {
                return;
            }
            boolean acquire = defaultRateLimiter.allow(resource, instance);
            if (!acquire) {
                logger.info("The resource: {} triggers flow control, the flow control instance is: {}", resource, instance);
                // Jump out of the loop
                throwRateLimitException();
            }
        });
    }

    private void throwRateLimitException() {
        RateLimiterProperties.Fallback fallback = defaultRateLimiter.fallback();
        final String code = Optional.ofNullable(fallback.getCode()).orElse(RateLimiterProperties.Fallback.DEFAULT_CODE);
        final String message = Optional.ofNullable(fallback.getMessage()).orElse(RateLimiterProperties.Fallback.DEFAULT_MESSAGE);
        throw new RateLimitException(code, message);
    }

    private String resolve(String instance, String originalResource, ProceedingJoinPoint pjp) {
        String limitType = defaultRateLimiter.getLimitType(instance);
        if (LimitTypeEnum.isCustomized(limitType)) {
            if (StringUtils.isBlank(originalResource)) {
                // 自定义资源时候，若对应资源为空，则解析时候取默认值
                limitType = LimitTypeEnum.DEFAULT.getCode();
            }
        }
        ResourceResolver resourceResolver = ResolverContext.getResourceResolver(limitType);
        return resourceResolver.resolve(originalResource, pjp);
    }

}
