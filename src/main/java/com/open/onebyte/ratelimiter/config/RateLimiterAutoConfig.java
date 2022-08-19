package com.open.onebyte.ratelimiter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.open.onebyte.ratelimiter.aspect.RateLimiterAspect;
import com.open.onebyte.ratelimiter.config.properties.RateLimiterProperties;
import com.open.onebyte.ratelimiter.core.DefaultRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * Flow control configuration
 *
 * @author yangqk
 * @date 2022/2/17
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({RateLimiterProperties.class, RedisProperties.class})
@ConditionalOnProperty(prefix = RateLimiterProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class RateLimiterAutoConfig {

    private final RateLimiterProperties rateLimiterProperties;

    public RateLimiterAutoConfig(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    @Bean
    public RateLimiterAspect rateLimiterAspect() {
        return new RateLimiterAspect(rateLimiter());
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultRateLimiter rateLimiter() {
        return new DefaultRateLimiter(rateLimiterProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimiterRedisConfig rateLimiterRedisConfig(@Autowired(required = false) RedisConnectionFactory redisConnectionFactory) {
        return new RateLimiterRedisConfig(redisTemplate(redisConnectionFactory));
    }

    @Bean(name = "rateLimiterRedisTemplate")
    @ConditionalOnMissingBean(name = "rateLimiterRedisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(@Autowired(required = false) RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 如果是空对象的时候,不抛异常
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
