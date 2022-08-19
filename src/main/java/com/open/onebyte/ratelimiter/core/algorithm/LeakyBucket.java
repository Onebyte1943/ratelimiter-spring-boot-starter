package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.annotation.AlgorithmType;
import com.open.onebyte.ratelimiter.config.RateLimiterRedisConfig;
import com.open.onebyte.ratelimiter.core.algorithm.param.LeakyBucketParam;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import com.open.onebyte.ratelimiter.exception.RateLimitException;
import com.open.onebyte.ratelimiter.model.constant.ScriptConstant;
import com.open.onebyte.ratelimiter.model.enums.AlgorithmEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author yangqk
 */
@Component
@AlgorithmType(algorithm = AlgorithmEnum.LEAKY_BUCKET)
public class LeakyBucket extends AbstractAlgorithm<LeakyBucketParam> {

    private static final Logger logger = LoggerFactory.getLogger(LeakyBucket.class);

    @Override
    public boolean allow(LeakyBucketParam param) {
        return allow(param.getResourceKey(), param.getCapacity(), param.getLeakingRate());
    }

    public boolean allow(String resourceKey, int capacity, int leakingRate) throws RateLimitException {
        boolean acquire = false;
        try {
            List<String> keys = this.getKeys(resourceKey, AlgorithmEnum.LEAKY_BUCKET.getCode());
            // The arguments to the LUA script.
            Object[] scriptArgs = {capacity, leakingRate, System.currentTimeMillis()};
            Long execute = redisTemplate.execute(redisScript, keys, scriptArgs);
            if (Objects.nonNull(execute) && execute.intValue() > 0) {
                acquire = true;
            }
        } catch (Exception e) {
            logger.error("LeakyBucket execute lua script error.", e);
            throw new ExecuteException(e);
        }
        return acquire;
    }

    @Override
    protected List<String> getKeys(String resourceKey, String algorithm) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "flow:limiter:{" + resourceKey + "}:" + algorithm;

        // You need two Redis keys for leaky Bucket.
        String quotaKey = prefix + ":quota";
        String timestampKey = prefix + ":timestamp";
        return Arrays.asList(quotaKey, timestampKey);
    }

    @Override
    protected String buildLuaScriptPath() {
        return ScriptConstant.LEAKY_BUCKET_ALGORITHM;
    }

    public LeakyBucket(RateLimiterRedisConfig rateLimiterRedisConfig) {
        super(rateLimiterRedisConfig);
    }
}
