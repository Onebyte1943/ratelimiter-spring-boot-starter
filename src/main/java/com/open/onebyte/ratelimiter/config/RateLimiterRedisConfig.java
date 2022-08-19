package com.open.onebyte.ratelimiter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;


/**
 * @author yangqk
 */
public class RateLimiterRedisConfig implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterRedisConfig.class);

    private final RedisTemplate<String, Serializable> redisTemplate;

    public RateLimiterRedisConfig(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, Serializable> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("RateLimiterRedisConfig completes initialization.");
        if (Objects.nonNull(redisTemplate)) {
            Properties info = redisTemplate.getRequiredConnectionFactory().getConnection().info("server");
            if (Objects.nonNull(info)) {
                info.forEach((k, v) -> logger.info("The redis server detail info: key is {}, value is {}.", k, v));
            }
        }
    }
}
