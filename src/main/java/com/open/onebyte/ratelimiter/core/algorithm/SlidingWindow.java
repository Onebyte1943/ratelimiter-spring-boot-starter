package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.annotation.AlgorithmType;
import com.open.onebyte.ratelimiter.config.RateLimiterRedisConfig;
import com.open.onebyte.ratelimiter.core.algorithm.param.SlidingWindowParam;
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
 * 滑动窗口算法，此处的实现与滑动窗口日志算法一样
 *
 * @author yangqk
 */
@Component
@AlgorithmType(algorithm = AlgorithmEnum.SLIDING_WINDOW)
public class SlidingWindow extends AbstractAlgorithm<SlidingWindowParam> {

    private static final Logger logger = LoggerFactory.getLogger(SlidingWindow.class);

    @Override
    public boolean allow(SlidingWindowParam param) {
        return allow(param.getResourceKey(), param.getCount(), param.getPeriod(), param.getChildWindowNum());
    }

    public boolean allow(String resourceKey, int count, int period, int childWindowNum) throws RateLimitException {
        boolean acquire = false;
        try {
            List<String> keys = this.getKeys(resourceKey, AlgorithmEnum.SLIDING_WINDOW.getCode());
            // The arguments to the LUA script.
            Object[] scriptArgs = {count, period, childWindowNum, System.currentTimeMillis()};
            Long execute = redisTemplate.execute(redisScript, keys, scriptArgs);
            if (Objects.nonNull(execute) && execute.intValue() > 0) {
                acquire = true;
            }
        } catch (Exception e) {
            logger.error("SlidingWindow execute lua script error.", e);
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
        String resourceRedisKey = prefix + ":resource";
        String timestampKey = prefix + ":timestamp";
        return Arrays.asList(resourceRedisKey, timestampKey);
    }

    @Override
    protected String buildLuaScriptPath() {
        return ScriptConstant.SLIDING_WINDOW_ALGORITHM;
    }

    public SlidingWindow(RateLimiterRedisConfig rateLimiterRedisConfig) {
        super(rateLimiterRedisConfig);
    }
}
