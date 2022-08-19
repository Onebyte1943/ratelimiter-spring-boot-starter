package com.open.onebyte.ratelimiter.support;

import com.open.onebyte.ratelimiter.annotation.RateLimiter;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import com.open.onebyte.ratelimiter.exception.RateLimitException;
import com.open.onebyte.ratelimiter.model.enums.FallbackStrategyEnum;
import com.open.onebyte.ratelimiter.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Some common functions for RateLimiter annotation aspect.
 *
 * @author yangqk
 */
public class RateLimiterAspectSupport extends AbstractRateLimiterSupport {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterAspectSupport.class);

    protected Object handleRateLimitException(ProceedingJoinPoint pjp, RateLimiter annotation, RateLimitException ex) throws Throwable {
        // Fail fast
        FallbackStrategyEnum strategy = annotation.fallbackStrategy();
        if (FallbackStrategyEnum.FAIL_FAST.equals(strategy)) {
            throw ex;
        }
        // Execute fallbackHandlerException handler if configured.
        Method fallbackHandlerMethod = extractFallbackHandlerMethod(pjp, annotation.fallbackHandler(), annotation.fallbackHandlerClass());
        if (fallbackHandlerMethod != null) {
            Object[] originArgs = pjp.getArgs();
            // Construct args.
            Object[] args = Arrays.copyOf(originArgs, originArgs.length + 1);
            args[args.length - 1] = ex;
            try {
                if (isStatic(fallbackHandlerMethod)) {
                    return fallbackHandlerMethod.invoke(null, args);
                }
                return fallbackHandlerMethod.invoke(pjp.getTarget(), args);
            } catch (InvocationTargetException e) {
                // throw the actual exception
                throw e.getTargetException();
            }
        }

        // If any handler does not exist, then directly throw the exception.
        throw ex;
    }

    protected Object handleExecuteException(ProceedingJoinPoint pjp, ExecuteException e) throws Throwable {
        logger.error("Flow control component internal exception.", e);
        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            if (ex instanceof RuntimeException) {
                throw new RuntimeException(ex);
            }
            throw ex;
        }
    }

    private Method extractFallbackHandlerMethod(ProceedingJoinPoint pjp, String name, Class<?>[] locationClass) {
        if (StringUtil.isBlank(name)) {
            return null;
        }

        boolean mustStatic = locationClass != null && locationClass.length >= 1;
        Class<?> clazz;
        if (mustStatic) {
            clazz = locationClass[0];
        } else {
            // By default current class.
            clazz = pjp.getTarget().getClass();
        }
        MethodWrapper m = ResourceMetadataRegistry.lookupFallbackHandler(clazz, name);
        if (m == null) {
            // First time, resolve the fallbackHandler handler.
            Method method = resolveFallbackHandlerInternal(pjp, name, clazz, mustStatic);
            // Cache the method instance.
            ResourceMetadataRegistry.updateFallbackHandlerFor(clazz, name, method);
            return method;
        }
        if (!m.isPresent()) {
            return null;
        }
        return m.getMethod();
    }

    private Method resolveFallbackHandlerInternal(ProceedingJoinPoint pjp, /*@NonNull*/ String name, Class<?> clazz,
                                                  boolean mustStatic) {
        Method originMethod = resolveMethod(pjp);
        Class<?>[] originList = originMethod.getParameterTypes();
        Class<?>[] parameterTypes = Arrays.copyOf(originList, originList.length + 1);
        parameterTypes[parameterTypes.length - 1] = RateLimitException.class;
        return findMethod(mustStatic, clazz, name, originMethod.getReturnType(), parameterTypes);
    }

}
