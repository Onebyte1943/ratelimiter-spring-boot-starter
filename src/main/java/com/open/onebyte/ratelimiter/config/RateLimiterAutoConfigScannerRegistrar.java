package com.open.onebyte.ratelimiter.config;

import com.open.onebyte.ratelimiter.annotation.EnableRedisRateLimiter;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * @author yangqk
 */
public class RateLimiterAutoConfigScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String SCAN_PATH = "com.open.onebyte.ratelimiter.*";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableRedisRateLimiter.class.getCanonicalName());
        if (Objects.isNull(attributes)) {
            return;
        }
        boolean enabled = (boolean) attributes.get("enabled");
        if (enabled) {
            ClassPathBeanDefinitionScanner scanConfigure = new ClassPathBeanDefinitionScanner(registry, true);
            scanConfigure.scan(SCAN_PATH);
        }
    }
}
