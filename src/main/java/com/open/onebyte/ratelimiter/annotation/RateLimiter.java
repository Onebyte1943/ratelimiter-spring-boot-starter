package com.open.onebyte.ratelimiter.annotation;

import com.open.onebyte.ratelimiter.model.enums.FallbackStrategyEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The core annotation of RateLimiter
 *
 * @author yangqk
 * @date 2022/2/17
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /**
     * The protected resource
     *
     * @return the protected resource
     */
    String[] resources() default {};

    /**
     * The instances of limit rule
     *
     * @return the instances of limit rule
     */
    String[] instances() default {};

    /**
     * The fallbackStrategy
     *
     * @return the fallbackStrategy
     */
    FallbackStrategyEnum fallbackStrategy() default FallbackStrategyEnum.FAIL_FAST;

    /**
     * Rate limit fallback method
     *
     * @return the handler method
     */
    String fallbackHandler() default "";

    /**
     * Rate limit fallback class
     *
     * @return the handler class
     */
    Class<?>[] fallbackHandlerClass() default {};
}
