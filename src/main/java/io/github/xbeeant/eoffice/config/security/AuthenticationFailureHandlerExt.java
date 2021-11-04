package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.antdesign.LoginResponse;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.security.handler.AuthenticationFailedHandler;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaobiao
 * @version 2021/6/30
 */
public class AuthenticationFailureHandlerExt extends AuthenticationFailedHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LoginResponse antLoginResponse = new LoginResponse();
        antLoginResponse.setStatus("error");
        antLoginResponse.setType("account");
        // 返回json
        Requests.writeJson(response, antLoginResponse);
    }
}

