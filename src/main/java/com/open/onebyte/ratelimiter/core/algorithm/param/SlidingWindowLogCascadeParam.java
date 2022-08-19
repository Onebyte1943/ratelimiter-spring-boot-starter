package com.open.onebyte.ratelimiter.core.algorithm.param;

/**
 * Sliding window log cascade algorithm param
 *
 * @author yangqk
 * @date 2022/2/17
 */
public class SlidingWindowLogCascadeParam extends AbstractAlgorithmParam {

    private int firstPeriod;
    private int firstCount;

    private int secondPeriod;
    private int secondCount;

    @Override
    public void defaultValueCheck() {
        if (firstPeriod == 0 || firstCount == 0
                || secondPeriod == 0 || secondCount == 0) {
            throw new IllegalArgumentException("SlidingWindowLogCascade algorithm input parameters are illegal, please check the configuration");
        }
    }

    public int getFirstPeriod() {
        return firstPeriod;
    }

    public void setFirstPeriod(int firstPeriod) {
        this.firstPeriod = firstPeriod;
    }

    public int getFirstCount() {
        return firstCount;
    }

    public void setFirstCount(int firstCount) {
        this.firstCount = firstCount;
    }

    public int getSecondPeriod() {
        return secondPeriod;
    }

    public void setSecondPeriod(int secondPeriod) {
        this.secondPeriod = secondPeriod;
    }

    public int getSecondCount() {
        return secondCount;
    }

    public void setSecondCount(int secondCount) {
        this.secondCount = secondCount;
    }
}
