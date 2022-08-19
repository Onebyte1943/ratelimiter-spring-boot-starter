package com.open.onebyte.ratelimiter.exception;

import com.open.onebyte.ratelimiter.model.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Springmvc 全局异常处理
 *
 * @author yangqk
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(RateLimitException.class)
    public ResultVO<String> hsMedExceptionHandler(HttpServletRequest request, RateLimitException rateLimitException) {
        logger.error("Flow-Control-Protected interface uri: {} message：{}", request.getRequestURI(), rateLimitException.getMessage(), rateLimitException);
        String code = rateLimitException.getCode();
        String message = rateLimitException.getMessage();
        return ResultVO.ofFail(code, message);
    }

}
