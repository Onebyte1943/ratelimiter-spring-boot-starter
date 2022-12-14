/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.open.onebyte.ratelimiter.support;

import com.open.onebyte.ratelimiter.util.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for resource configuration metadata (e.g. handler method)
 *
 * @author yangqk
 */
final class ResourceMetadataRegistry {

    private static final Map<String, MethodWrapper> FALLBACK_HANDLER_MAP = new ConcurrentHashMap<>();

    static MethodWrapper lookupFallbackHandler(Class<?> clazz, String name) {
        return FALLBACK_HANDLER_MAP.get(getKey(clazz, name));
    }

    static void updateFallbackHandlerFor(Class<?> clazz, String name, Method method) {
        if (clazz == null || StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Bad argument");
        }
        FALLBACK_HANDLER_MAP.put(getKey(clazz, name), MethodWrapper.wrap(method));
    }

    private static String getKey(Class<?> clazz, String name) {
        return String.format("%s:%s", clazz.getCanonicalName(), name);
    }

}
