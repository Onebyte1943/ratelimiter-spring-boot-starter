package com.open.onebyte.ratelimiter.exception;


/**
 * ExecuteException
 *
 * @author yangqk
 */
public class ExecuteException extends RuntimeException {

    public ExecuteException() {
    }

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }
}
