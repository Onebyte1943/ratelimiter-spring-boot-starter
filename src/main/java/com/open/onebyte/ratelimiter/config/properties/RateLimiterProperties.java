package com.open.onebyte.ratelimiter.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangqk
 */
@Component
@ConfigurationProperties(prefix = RateLimiterProperties.PREFIX)
public class RateLimiterProperties {

    public static final String PREFIX = "flow.limiter";

    private boolean enabled;
    private Fallback fallback = new Fallback();
    private Map<String, InstanceProperties> instances = new HashMap<>();

    @Nullable
    public InstanceProperties getInstanceProperties(String instance) {
        return instances.get(instance);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Fallback getFallback() {
        return fallback;
    }

    public void setFallback(Fallback fallback) {
        this.fallback = fallback;
    }

    public Map<String, InstanceProperties> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, InstanceProperties> instances) {
        this.instances = instances;
    }

    public static class Fallback {
        public static final String DEFAULT_CODE = "10000";
        public static final String DEFAULT_MESSAGE = "Frequent requests, please try again later.";

        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
