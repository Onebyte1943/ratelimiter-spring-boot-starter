package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.annotation.AlgorithmType;
import com.open.onebyte.ratelimiter.config.RateLimiterRedisConfig;
import com.open.onebyte.ratelimiter.core.algorithm.param.FixedWindowParam;
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
@AlgorithmType(algorithm = AlgorithmEnum.FIXED_WINDOW)
public class FixedWindow extends AbstractAlgorithm<FixedWindowParam> {

    private static final Logger logger = LoggerFactory.getLogger(FixedWindow.class);

    @Override
    public boolean allow(FixedWindowParam param) {
        return allow(param.getResourceKey(), param.getCount(), param.getPeriod());
    }

    public boolean allow(String resourceKey, int count, int period) throws RateLimitException {
        boolean acquire = false;
        try {
            List<String> keys = getKeys(resourceKey, AlgorithmEnum.FIXED_WINDOW.getCode());
            // The arguments to the LUA script.
            Object[] scriptArgs = {count, period};
            Long execute = redisTemplate.execute(redisScript, keys, scriptArgs);
            if (Objects.nonNull(execute) && execute.intValue() > 0) {
                acquire = true;
            }
        } catch (Exception e) {
            logger.error("FixedWindow execute lua script error.", e);
            throw new ExecuteException(e);
        }
        return acquire;
    }


    @Override
    protected String buildLuaScriptPath() {
        return ScriptConstant.FIXED_WINDOW_ALGORITHM;
    }

    public FixedWindow(RateLimiterRedisConfig rateLimiterRedisConfig) {
        super(rateLimiterRedisConfig);
    }
}
