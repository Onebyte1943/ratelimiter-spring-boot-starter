package com.open.onebyte.ratelimiter.core;

import com.open.onebyte.ratelimiter.api.Limiter;
import com.open.onebyte.ratelimiter.config.properties.InstanceProperties;
import com.open.onebyte.ratelimiter.config.properties.RateLimiterProperties;
import com.open.onebyte.ratelimiter.core.algorithm.param.AbstractAlgorithmParam;
import com.open.onebyte.ratelimiter.exception.RateLimitException;
import com.open.onebyte.ratelimiter.factory.AlgorithmContext;

import java.util.Objects;

/**
 * ClusterRateLimiter
 *
 * @author yangqk
 * @date 2022/2/18
 */
public class DefaultRateLimiter implements Limiter {

    private final RateLimiterProperties RateLimiterProperties;

    public DefaultRateLimiter(RateLimiterProperties rateLimiterProperties) {
        RateLimiterProperties = rateLimiterProperties;
    }

    @Override
    public boolean allow(String resource, String instance) throws RateLimitException {
        InstanceProperties instanceProperties = getInstanceProperties(instance);
        AlgorithmContext.AlgorithmWrapper wrapper = AlgorithmContext.getAlgorithmWrapper(instance, instanceProperties.getAlgorithm());
        AbstractAlgorithmParam param = wrapper.getAlgorithmParam();
        param.setResourceKey(resource);
        return wrapper.getAlgorithm().allow(param);
    }

    public InstanceProperties getInstanceProperties(String instance) {
        InstanceProperties instanceProperties = RateLimiterProperties.getInstanceProperties(instance);
        if (Objects.isNull(instanceProperties)) {
            throw new IllegalArgumentException("Invalid instance, unable to find instance information for instance.");
        }
        return instanceProperties;
    }

    public String getLimitType(String instance) {
        return getInstanceProperties(instance).getLimitType();
    }

    public RateLimiterProperties.Fallback fallback() {
        return RateLimiterProperties.getFallback();
    }
}
