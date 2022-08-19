package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.config.RateLimiterRedisConfig;
import com.open.onebyte.ratelimiter.core.algorithm.param.AlgorithmParam;
import com.open.onebyte.ratelimiter.exception.ExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author yangqk
 */
public abstract class AbstractAlgorithm<T extends AlgorithmParam> implements Algorithm<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAlgorithm.class);

    /**
     * RedisScript
     */
    protected RedisScript<Long> redisScript;
    /**
     * RateLimiterRedisConfig
     */
    protected final RateLimiterRedisConfig rateLimiterRedisConfig;
    /**
     * RedisTemplate
     */
    protected final RedisTemplate<String, Serializable> redisTemplate;

    public AbstractAlgorithm(RateLimiterRedisConfig rateLimiterRedisConfig) {
        this.rateLimiterRedisConfig = rateLimiterRedisConfig;
        this.redisTemplate = rateLimiterRedisConfig.getRedisTemplate();
    }

    /**
     * Build lua
     *
     * @return lua
     */
    protected abstract String buildLuaScriptPath();

    protected RedisScript<Long> loadRedisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource(buildLuaScriptPath())));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    protected List<String> getKeys(String resourceKey, String algorithm) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String key = "flow:limiter:{" + resourceKey + "}:" + algorithm;
        return Collections.singletonList(key);
    }

    @PostConstruct
    protected void initRedisScript() {
        redisScript = loadRedisScript();
    }

    @Deprecated
    protected String loadLuaScript(String path) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
            Resource[] resource = resolver.getResources(path);
            String luaScript = StreamUtils.copyToString(resource[0].getInputStream(), StandardCharsets.UTF_8);
            logger.debug("The lua script path is: {}, content is: {}.", path, luaScript);
            return luaScript;
        } catch (IOException e) {
            logger.error("Load lua script error.", e);
            throw new ExecuteException("Load lua script error!", e);
        }
    }
}
