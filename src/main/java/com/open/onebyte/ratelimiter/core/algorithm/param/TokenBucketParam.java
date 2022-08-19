package com.open.onebyte.ratelimiter.core.algorithm.param;

/**
 * Token bucket algorithm param
 *
 * @author yangqk
 * @date 2022/2/17
 */
public class TokenBucketParam extends AbstractAlgorithmParam {

    /**
     * How many requests per second do you want a user to be allowed to do?
     * 令牌补充速率
     */
    private int replenishRate;

    /**
     * How much bursting do you want to allow?
     * 突发容量
     */
    private int burstCapacity;

    /**
     * How many tokens are requested per request?
     * 每次请求所使用的令牌数目
     */
    private int requestedTokens;

    @Override
    public void defaultValueCheck() {
        if (replenishRate == 0 || burstCapacity == 0) {
            throw new IllegalArgumentException("TokenBucket algorithm input parameters are illegal, please check the configuration");
        }
        if (requestedTokens == 0) {
            requestedTokens = 1;
        }
    }

    public int getReplenishRate() {
        return replenishRate;
    }

    public void setReplenishRate(int replenishRate) {
        this.replenishRate = replenishRate;
    }

    public int getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(int burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    public int getRequestedTokens() {
        return requestedTokens;
    }

    public void setRequestedTokens(int requestedTokens) {
        this.requestedTokens = requestedTokens;
    }
}
