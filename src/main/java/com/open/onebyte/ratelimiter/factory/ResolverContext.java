package com.open.onebyte.ratelimiter.factory;

import com.open.onebyte.ratelimiter.annotation.ResolverType;
import com.open.onebyte.ratelimiter.core.resolver.ResourceResolver;
import com.open.onebyte.ratelimiter.model.enums.LimitTypeEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangqk
 */
@Component
public class ResolverContext implements InitializingBean, ApplicationContextAware {

    private static final Map<LimitTypeEnum, ResourceResolver> RESOLVER_STRATEGIES = new ConcurrentHashMap<>(5);

    private ApplicationContext appContext;

    public static ResourceResolver getResourceResolver(String limitType) {
        return RESOLVER_STRATEGIES.get(LimitTypeEnum.getLimitType(limitType));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // instance
        Map<String, ResourceResolver> resolverMap = appContext.getBeansOfType(ResourceResolver.class);
        if (resolverMap.isEmpty()) {
            return;
        }

        resolverMap.forEach((k, v) -> {
            boolean present = v.getClass().isAnnotationPresent(ResolverType.class);
            if (present) {
                ResolverType resolverType = v.getClass().getAnnotation(ResolverType.class);
                if (Objects.isNull(resolverType.limitType())) {
                    throw new IllegalArgumentException("LimitType is a required field.");
                }
                RESOLVER_STRATEGIES.put(resolverType.limitType(), v);
            } else {
                throw new IllegalArgumentException("The newly added resolver class must be marked by the annotation @ResolverType");
            }
        });
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

}
