package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.antdesign.LoginResponse;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.security.handler.AuthenticationSuccessHandler;
import org.apache.http.HttpStatus;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaobiao
 * @version 2021/9/30
 */
public class AuthenticationSuccessHandlerExt extends AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {

        request.getSession(true);
        LoginResponse antLoginResponse = SecurityHelper.antLoginResponse(authentication, request);
        // 输出登录提示信息
        response.setStatus(HttpStatus.SC_OK);
        SecurityUser<User> principal = (SecurityUser<User>) authentication.getPrincipal();
        String userId = principal.getUserId();
        // todo 行为日志


        // 返回json
        Requests.writeJson(response, antLoginResponse);
    }
}
