package com.open.onebyte.ratelimiter.config.properties;

import com.open.onebyte.ratelimiter.core.algorithm.param.*;

/**
 * @author yuntai
 */
public class InstanceProperties {

    private String algorithm;
    private String limitType;
    private int order;
    private FixedWindowParam fixedWindow = new FixedWindowParam();
    private LeakyBucketParam leakyBucket = new LeakyBucketParam();
    private SlidingWindowParam slidingWindow = new SlidingWindowParam();
    private SlidingWindowLogParam slidingWindowLog = new SlidingWindowLogParam();
    private TokenBucketParam tokenBucket = new TokenBucketParam();
    private SlidingWindowLogCascadeParam slidingWindowLogCascade = new SlidingWindowLogCascadeParam();

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public FixedWindowParam getFixedWindow() {
        return fixedWindow;
    }

    public void setFixedWindow(FixedWindowParam fixedWindow) {
        this.fixedWindow = fixedWindow;
    }

    public LeakyBucketParam getLeakyBucket() {
        return leakyBucket;
    }

    public void setLeakyBucket(LeakyBucketParam leakyBucket) {
        this.leakyBucket = leakyBucket;
    }

    public SlidingWindowParam getSlidingWindow() {
        return slidingWindow;
    }

    public void setSlidingWindow(SlidingWindowParam slidingWindow) {
        this.slidingWindow = slidingWindow;
    }

    public SlidingWindowLogParam getSlidingWindowLog() {
        return slidingWindowLog;
    }

    public void setSlidingWindowLog(SlidingWindowLogParam slidingWindowLog) {
        this.slidingWindowLog = slidingWindowLog;
    }

    public TokenBucketParam getTokenBucket() {
        return tokenBucket;
    }

    public void setTokenBucket(TokenBucketParam tokenBucket) {
        this.tokenBucket = tokenBucket;
    }

    public SlidingWindowLogCascadeParam getSlidingWindowLogCascade() {
        return slidingWindowLogCascade;
    }

    public void setSlidingWindowLogCascade(SlidingWindowLogCascadeParam slidingWindowLogCascade) {
        this.slidingWindowLogCascade = slidingWindowLogCascade;
    }
}
