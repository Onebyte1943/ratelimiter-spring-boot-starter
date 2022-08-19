package com.open.onebyte.ratelimiter.exception;

/**
 * RateLimitException
 *
 * @author yangqk
 */
public class RateLimitException extends RuntimeException {

    private String code;

    public RateLimitException() {
    }

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
