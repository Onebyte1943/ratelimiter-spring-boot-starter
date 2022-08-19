package com.open.onebyte.ratelimiter.core.algorithm.param;

/**
 * Sliding window algorithm param
 *
 * @author yangqk
 * @date 2022/2/17
 */
public class SlidingWindowParam extends AbstractAlgorithmParam {

    private static final int DEFAULT_PERIOD = 1;
    private static final int DEFAULT_COUNT = 10000;
    private static final int DEFAULT_CHILD_WINDOW_NUM = 10;

    private int period;
    private int count;
    private int childWindowNum;

    @Override
    public void defaultValueCheck() {
        if (period == 0) {
            period = DEFAULT_PERIOD;
        }
        if (count == 0) {
            count = DEFAULT_COUNT;
        }
        if (childWindowNum == 0) {
            childWindowNum = DEFAULT_CHILD_WINDOW_NUM;
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

    public int getChildWindowNum() {
        return childWindowNum;
    }

    public void setChildWindowNum(int childWindowNum) {
        this.childWindowNum = childWindowNum;
    }
}
