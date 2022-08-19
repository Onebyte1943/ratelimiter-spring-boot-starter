package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.annotation.AlgorithmType;
import com.open.onebyte.ratelimiter.config.RateLimiterRedisConfig;
import com.open.onebyte.ratelimiter.core.algorithm.param.TokenBucketParam;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import com.open.onebyte.ratelimiter.exception.RateLimitException;
import com.open.onebyte.ratelimiter.model.constant.ScriptConstant;
import com.open.onebyte.ratelimiter.model.enums.AlgorithmEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author yangqk
 */
@Component
@AlgorithmType(algorithm = AlgorithmEnum.TOKEN_BUCKET)
public class TokenBucket extends AbstractAlgorithm<TokenBucketParam> {

    private static final Logger logger = LoggerFactory.getLogger(TokenBucket.class);

    @Override
    public boolean allow(TokenBucketParam param) {
        return allow(param.getResourceKey(), param.getReplenishRate(), param.getBurstCapacity(), param.getRequestedTokens());
    }

    public boolean allow(String resourceKey, int replenishRate, int burstCapacity, int requestedTokens) throws RateLimitException {
        boolean acquire = false;
        try {
            List<String> keys = this.getKeys(resourceKey, AlgorithmEnum.TOKEN_BUCKET.getCode());
            // The arguments to the LUA script. time() returns unixtime in seconds.
            Object[] scriptArgs = {replenishRate, burstCapacity, Instant.now().getEpochSecond(), requestedTokens};
            Long execute = redisTemplate.execute(redisScript, keys, scriptArgs);
            if (Objects.nonNull(execute) && execute.intValue() > 0) {
                acquire = true;
            }
        } catch (Exception e) {
            logger.error("TokenBucket execute lua script error.", e);
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

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + ":tokens";
        String timestampKey = prefix + ":timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    @Override
    protected String buildLuaScriptPath() {
        return ScriptConstant.TOKEN_BUCKET_ALGORITHM;
    }

    public TokenBucket(RateLimiterRedisConfig rateLimiterRedisConfig) {
        super(rateLimiterRedisConfig);
    }
}
