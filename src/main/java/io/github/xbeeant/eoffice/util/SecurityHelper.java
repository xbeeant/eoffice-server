package io.github.xbeeant.eoffice.util;

import io.github.xbeeant.antdesign.LoginResult;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IUserService;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.security.UserHelper;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

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



    /**
     * 通过Token 或者 Authorization
     *
     * @param request HttpServletRequest
     * @return
     */
    public static SecurityUser<User> tokenToUser(HttpServletRequest request) {
        SecurityUser<User> currentAuthUser = null;
        String token = request.getParameter("token");
        if (token != null) {
            String sUserId = Base64Helper.base64Decode(token);
            ApiResponse<User> userApiResponse = SpringContextProvider.getBean(IUserService.class).selectByPrimaryKey(Long.valueOf(sUserId));
            User user = userApiResponse.getData();
            user.setPassword("");
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            SecurityUser<User> userSecurityUser = new SecurityUser(String.valueOf(user.getUid()), user.getNickname(), user.getUsername(), user.getPassword(), grantedAuthorities);
            userSecurityUser.setDetails(user);
            return userSecurityUser;
        }

        return currentAuthUser;
    }
}
