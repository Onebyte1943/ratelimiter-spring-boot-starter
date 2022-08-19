package com.open.onebyte.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The core annotation of SlidingWindowLogRateLimiter
 *
 * @author yangqk
 * @date 2022/2/17
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SlidingWindowLogRateLimiter {

    /**
     * The protected resource
     *
     * @return the protected resource
     */
    String resource() default "";

    /**
     * The period of count
     *
     * @return the period of count
     */
    int period() default 1;

    /**
     * The max value
     *
     * @return the max value
     */
    int count();

    /**
     * The mode of rate limiter: stand-alone, cluster
     *
     * @return the mode of rate limiter
     */
    String mode() default "";

    /**
     * The algorithm of rate limiter
     *
     * @return the algorithm of rate limiter
     */
    String algorithm() default "";

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
