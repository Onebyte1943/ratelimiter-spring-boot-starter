package com.open.onebyte.ratelimiter.annotation;

import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The resolver type
 *
 * @author yangqk
 * @date 2022/2/17
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ResolverType {

    LimitTypeEnum limitType();

}
