package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.http.Requests;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaobiao
 * @version 2021/10/30
 */
public class EofficeUnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ApiResponse<String> msg = new ApiResponse<>(401, "尚未登录");
        response.setStatus(HttpStatus.SC_OK);
        Requests.writeJson(response, msg);
    }
}
