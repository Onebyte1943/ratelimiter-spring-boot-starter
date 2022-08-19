package com.open.onebyte.ratelimiter.core.algorithm.param;

/**
 * @author yangqk
 */
public abstract class AbstractAlgorithmParam implements AlgorithmParam {

    private String resourceKey;

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }
}
