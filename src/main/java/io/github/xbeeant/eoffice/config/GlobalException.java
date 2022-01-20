package io.github.xbeeant.eoffice.config;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.exception.InvalidActionException;
import io.github.xbeeant.eoffice.exception.ResourceMissingException;
import org.apache.ibatis.reflection.ReflectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaobiao
 * @version 2021/11/3
 */
@ControllerAdvice
@ResponseBody
public class GlobalException {
    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    /**
     * reflection exception handler
     *
     * @param request 请求
     * @param e       e
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    @ExceptionHandler(value = ReflectionException.class)
    public ApiResponse<String> reflectionExceptionHandler(HttpServletRequest request, Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(500, "SQL异常");
        logger.error("SQL异常", e);
        return apiResponse;
    }

    @ExceptionHandler(value = InvalidActionException.class)
    public ApiResponse<String> invalidExceptionHandler(HttpServletRequest request, Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(400, e.getMessage());
        return apiResponse;
    }

    /**
     * folder missing exception handler
     *
     * @param request 请求
     * @param e       e
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    @ExceptionHandler(value = ResourceMissingException.class)
    public ApiResponse<String> resourceMissingExceptionHandler(HttpServletRequest request, Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(500, e.getMessage());
        logger.error("SQL异常", e);
        return apiResponse;
    }
}
