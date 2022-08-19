package com.open.onebyte.ratelimiter.annotation;

import com.open.onebyte.ratelimiter.config.RateLimiterAutoConfigScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangqk
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Import({RateLimiterAutoConfigScannerRegistrar.class})
public @interface EnableRedisRateLimiter {

    boolean enabled() default true;
}
