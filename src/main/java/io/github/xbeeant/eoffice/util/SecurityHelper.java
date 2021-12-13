package io.github.xbeeant.eoffice.util;

import io.github.xbeeant.antdesign.LoginResult;
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

    public static LoginResult antLoginResult(HttpServletRequest request) {
        Authentication authentication = UserHelper.getCurrentUser();
        return antLoginResult(authentication, request);
    }

    public static LoginResult antLoginResult(Authentication authentication, HttpServletRequest request) {
        if (authentication.getPrincipal().equals(ANONYMOUS)) {
            LoginResult antLoginResult = new LoginResult();
            antLoginResult.setStatus("error");
            return antLoginResult;
        }
        LoginResult antLoginResult = new LoginResult();
        antLoginResult.setStatus("ok");
        antLoginResult.setCurrentAuthority("user");
        return antLoginResult;
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
