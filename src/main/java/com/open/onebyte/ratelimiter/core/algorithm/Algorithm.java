package com.open.onebyte.ratelimiter.core.algorithm;

import com.open.onebyte.ratelimiter.core.algorithm.param.AlgorithmParam;

/**
 * @author yangqk
 */
public interface Algorithm<T extends AlgorithmParam> {

    /**
     * 流控算法
     *
     * @param param the param of algorithm
     * @return whether through flow control
     */
    boolean allow(T param);
}
