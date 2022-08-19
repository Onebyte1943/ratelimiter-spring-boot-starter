package com.open.onebyte.ratelimiter.core.algorithm.param;

/**
 * Leaky bucket algorithm param
 *
 * @author yangqk
 * @date 2022/2/17
 */
public class LeakyBucketParam extends AbstractAlgorithmParam {

    private int capacity;
    private int leakingRate;

    @Override
    public void defaultValueCheck() {
        if (capacity == 0 || leakingRate == 0) {
            throw new IllegalArgumentException("LeakyBucket algorithm input parameters are illegal, please check the configuration");
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLeakingRate() {
        return leakingRate;
    }

    public void setLeakingRate(int leakingRate) {
        this.leakingRate = leakingRate;
    }
}
