package com.open.onebyte.ratelimiter.factory;

import com.open.onebyte.ratelimiter.annotation.AlgorithmType;
import com.open.onebyte.ratelimiter.config.properties.InstanceProperties;
import com.open.onebyte.ratelimiter.config.properties.RateLimiterProperties;
import com.open.onebyte.ratelimiter.core.InstanceEntry;
import com.open.onebyte.ratelimiter.core.algorithm.Algorithm;
import com.open.onebyte.ratelimiter.core.algorithm.param.AbstractAlgorithmParam;
import com.open.onebyte.ratelimiter.core.algorithm.param.AlgorithmParam;
import com.open.onebyte.ratelimiter.model.enums.AlgorithmEnum;
import com.open.onebyte.ratelimiter.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangqk
 */
@Component
public class AlgorithmContext implements InitializingBean, ApplicationContextAware {

    private static final Map<InstanceEntry, AlgorithmWrapper> ALGORITHM_STRATEGIES = new ConcurrentHashMap<>(16);

    private final RateLimiterProperties rateLimiterProperties;
    private ApplicationContext appContext;

    public static AlgorithmWrapper getAlgorithmWrapper(InstanceEntry instanceEntry) {
        return ALGORITHM_STRATEGIES.get(instanceEntry);
    }

    public static AlgorithmWrapper getAlgorithmWrapper(String instance, String algorithm) {
        InstanceEntry instanceEntry = new InstanceEntry();
        instanceEntry.setInstance(instance);
        instanceEntry.setAlgorithm(unifyUnderscore(algorithm));
        return getAlgorithmWrapper(instanceEntry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        // Algorithm strategies.
        Map<AlgorithmEnum, Algorithm> algorithmMap = getStrategies(Algorithm.class);
        // instances
        Map<String, InstanceProperties> instances = rateLimiterProperties.getInstances();
        instances.forEach((k, v) -> {
            String algorithm = unifyUnderscore(v.getAlgorithm());
            // InstanceEntry init.
            InstanceEntry instanceEntry = new InstanceEntry(k, algorithm);

            AlgorithmEnum algorithmEnum = AlgorithmEnum.getAlgorithmEnum(algorithm);
            // Wrap algorithm
            AlgorithmWrapper wrapper = new AlgorithmWrapper(getAlgorithmParam(v), algorithmMap.get(algorithmEnum));
            ALGORITHM_STRATEGIES.put(instanceEntry, wrapper);
        });
    }

    private AbstractAlgorithmParam getAlgorithmParam(InstanceProperties instanceProperties) {
        String originalAlgorithm = unifyUnderscore(instanceProperties.getAlgorithm());
        if (!AlgorithmEnum.isSupportAlgorithm(originalAlgorithm)) {
            throw new IllegalArgumentException("Unsupported flow control algorithm.");
        }

        String algorithm = StringUtil.toCamelCase(originalAlgorithm);
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(InstanceProperties.class);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("InstanceProperties illegal.");
        }

        PropertyDescriptor propertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors()).filter((descriptor) -> descriptor.getName().equals(algorithm)).findFirst().orElse(null);
        if (Objects.isNull(propertyDescriptor)) {
            throw new IllegalArgumentException("Algorithm name illegal.");
        }

        Method readMethod = propertyDescriptor.getReadMethod();
        AbstractAlgorithmParam algorithmParam = (AbstractAlgorithmParam) ReflectionUtils.invokeMethod(readMethod, instanceProperties);
        // Default value.
        if (Objects.nonNull(algorithmParam)) {
            algorithmParam.defaultValueCheck();
        }
        return algorithmParam;
    }

    public <T> Map<AlgorithmEnum, T> getStrategies(Class<T> clazz) {
        Map<AlgorithmEnum, T> strategies = new HashMap<>(8);
        Map<String, T> beans = appContext.getBeansOfType(clazz);
        if (beans.isEmpty()) {
            return strategies;
        }

        beans.forEach((k, v) -> {
            Class<?> targetClass = v.getClass();
            boolean present = targetClass.isAnnotationPresent(AlgorithmType.class);
            if (present) {
                AlgorithmType algorithmType = targetClass.getAnnotation(AlgorithmType.class);
                if (Objects.isNull(algorithmType.algorithm())) {
                    throw new IllegalArgumentException("AlgorithmType is a required field.");
                }
                strategies.put(algorithmType.algorithm(), v);
            } else {
                throw new IllegalArgumentException("The newly added algorithm class must be marked by the annotation @AlgorithmType");
            }
        });
        return strategies;
    }

    private static String unifyUnderscore(String str) {
        if (StringUtil.isBlank(str)) {
            return "";
        }
        return str.replace("-", "_");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    public AlgorithmContext(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    public static class AlgorithmWrapper {

        private AbstractAlgorithmParam algorithmParam;
        private Algorithm<AlgorithmParam> algorithm;

        public AlgorithmWrapper() {
        }

        public AlgorithmWrapper(AbstractAlgorithmParam algorithmParam, Algorithm<AlgorithmParam> algorithm) {
            this.algorithmParam = algorithmParam;
            this.algorithm = algorithm;
        }

        public AbstractAlgorithmParam getAlgorithmParam() {
            return algorithmParam;
        }

        public void setAlgorithmParam(AbstractAlgorithmParam algorithmParam) {
            this.algorithmParam = algorithmParam;
        }

        public Algorithm<AlgorithmParam> getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(Algorithm<AlgorithmParam> algorithm) {
            this.algorithm = algorithm;
        }
    }

}
