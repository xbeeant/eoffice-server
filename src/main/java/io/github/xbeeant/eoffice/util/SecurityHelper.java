package io.github.xbeeant.eoffice.util;

import io.github.xbeeant.antdesign.LoginResponse;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.security.UserHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaobiao
 * @version 2021/9/30
 */
public class SecurityHelper {
    private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

    private static final String ANONYMOUS = "anonymousUser";

    public static LoginResponse antLoginResponse(HttpServletRequest request) {
        Authentication authentication = UserHelper.getCurrentUser();
        return antLoginResponse(authentication, request);
    }

    public static LoginResponse antLoginResponse(Authentication authentication, HttpServletRequest request) {
        if (authentication.getPrincipal().equals(ANONYMOUS)) {
            LoginResponse antLoginResponse = new LoginResponse();
            antLoginResponse.setStatus("error");
            return antLoginResponse;
        }
        LoginResponse antLoginResponse = new LoginResponse();
        antLoginResponse.setStatus("ok");
        antLoginResponse.setCurrentAuthority("user");
        return antLoginResponse;
    }

    public static SecurityUser<User> currentUser() {
        Authentication currentUser = UserHelper.getCurrentUser();
        if (currentUser instanceof AnonymousAuthenticationToken) {
            throw new AccountExpiredException("登录已过期");
        }
        return (SecurityUser<User>) currentUser.getPrincipal();
    }

    /**
     * current user
     *
     * @param authentication 认证信息
     * @return {@link SecurityUser}
     * @see SecurityUser
     * @see User
     */
    public static SecurityUser<User> currentUser(Authentication authentication) {
        return (SecurityUser<User>) authentication.getPrincipal();
    }
}
