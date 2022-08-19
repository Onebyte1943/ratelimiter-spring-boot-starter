package com.open.onebyte.ratelimiter;

import com.open.onebyte.ratelimiter.config.properties.InstanceProperties;
import com.yuntai.udb.auth.sdk.boot.domain.util.UdbUserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class RateLimiterTest {

    @Test
    public void beanInfoTest() {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(InstanceProperties.class);
        } catch (IntrospectionException e) {
        }

        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        Arrays.stream(descriptors).forEach(
                (descriptor) -> System.out.println(descriptor.getName())
        );
    }

    @Test
    public void classNameTest() {
        String name = UdbUserUtil.class.getName();
        boolean present = ClassUtils.isPresent(name, ClassUtils.getDefaultClassLoader());
        Assert.assertTrue(present);
    }
}
