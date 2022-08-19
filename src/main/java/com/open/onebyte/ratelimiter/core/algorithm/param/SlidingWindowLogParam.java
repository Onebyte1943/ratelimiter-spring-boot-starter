package com.open.onebyte.ratelimiter.core.algorithm.param;

/**
 * Sliding window log algorithm param
 *
 * @author yangqk
 * @date 2022/2/17
 */
public class SlidingWindowLogParam extends AbstractAlgorithmParam {

    private static final int DEFAULT_PERIOD = 1;
    private static final int DEFAULT_COUNT = 10000;

    private int period;
    private int count;

    @Override
    public void defaultValueCheck() {
        if (period == 0) {
            period = DEFAULT_PERIOD;
        }
        if (count == 0) {
            count = DEFAULT_COUNT;
        }
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
