package com.open.onebyte.ratelimiter.api;

import com.open.onebyte.ratelimiter.exception.RateLimitException;

/**
 * RateLimiter
 *
 * @author yuntai
 */
public interface Limiter {

    /**
     * Flow control
     *
     * @param resource the resource
     * @param instance the instance of rateLimiter
     * @return the result of flow control
     * @throws RateLimitException the RateLimitException
     */
    boolean allow(String resource, String instance) throws RateLimitException;
}
