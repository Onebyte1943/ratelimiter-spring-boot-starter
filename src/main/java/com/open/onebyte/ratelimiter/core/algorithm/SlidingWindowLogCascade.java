package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.annotation.AlgorithmType;
import com.open.onebyte.ratelimiter.config.RateLimiterRedisConfig;
import com.open.onebyte.ratelimiter.core.algorithm.param.SlidingWindowLogCascadeParam;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import com.open.onebyte.ratelimiter.exception.RateLimitException;
import com.open.onebyte.ratelimiter.model.constant.ScriptConstant;
import com.open.onebyte.ratelimiter.model.enums.AlgorithmEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author yangqk
 */
@Component
@AlgorithmType(algorithm = AlgorithmEnum.SLIDING_WINDOW_LOG_CASCADE)
public class SlidingWindowLogCascade extends AbstractAlgorithm<SlidingWindowLogCascadeParam> {

    private static final Logger logger = LoggerFactory.getLogger(SlidingWindowLogCascade.class);

    @Override
    public boolean allow(SlidingWindowLogCascadeParam param) {
        return allow(param.getResourceKey(), param.getFirstCount(), param.getFirstPeriod(), param.getSecondCount(), param.getSecondPeriod());
    }

    public boolean allow(String resourceKey, int firstCount, int firstPeriod, int secondCount, int secondPeriod) throws RateLimitException {
        boolean acquire = false;
        try {
            List<String> keys = getKeys(resourceKey, AlgorithmEnum.SLIDING_WINDOW_LOG_CASCADE.getCode());
            // The arguments to the LUA script.
            Object[] scriptArgs = {firstCount, firstPeriod, secondCount, secondPeriod, System.currentTimeMillis()};
            Long execute = redisTemplate.execute(redisScript, keys, scriptArgs);
            if (Objects.nonNull(execute) && execute.intValue() > 0) {
                acquire = true;
            }
        } catch (Exception e) {
            logger.error("SlidingWindowLogCascade execute lua script error.", e);
            throw new ExecuteException(e);
        }
        return acquire;
    }

    @Override
    protected String buildLuaScriptPath() {
        return ScriptConstant.SLIDING_WINDOW_LOG_ALGORITHM;
    }

    public SlidingWindowLogCascade(RateLimiterRedisConfig rateLimiterRedisConfig) {
        super(rateLimiterRedisConfig);
    }
}
