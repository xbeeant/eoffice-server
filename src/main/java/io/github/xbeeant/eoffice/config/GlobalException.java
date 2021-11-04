package io.github.xbeeant.eoffice.config;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.exception.FolderMissingException;
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

    /**
     * folder missing exception handler
     *
     * @param request 请求
     * @param e       e
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    @ExceptionHandler(value = FolderMissingException.class)
    public ApiResponse<String> folderMissingExceptionHandler(HttpServletRequest request, Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(500, "父目录已经丢了哦，请返回首页重试");
        logger.error("SQL异常", e);
        return apiResponse;
    }
}
