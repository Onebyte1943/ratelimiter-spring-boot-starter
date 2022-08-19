package com.open.onebyte.ratelimiter.core;

import java.util.Objects;

/**
 * @author yangqk
 */
public class InstanceEntry {

    private String instance;
    private String algorithm;

    public InstanceEntry() {
    }

    public InstanceEntry(String instance, String algorithm) {
        this.instance = instance;
        this.algorithm = algorithm;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceEntry that = (InstanceEntry) o;
        return getInstance().equals(that.getInstance()) && getAlgorithm().equals(that.getAlgorithm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInstance(), getAlgorithm());
    }
}
