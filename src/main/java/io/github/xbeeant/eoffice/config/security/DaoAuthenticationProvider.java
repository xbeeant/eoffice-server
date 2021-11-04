package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.eoffice.service.IUserService;
import io.github.xbeeant.spring.security.LoginParamters;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author xiaobiao
 * @version 2021/6/30
 */
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    /**
     * 日志
     *
     */
    private static final Logger log = LoggerFactory.getLogger(DaoAuthenticationProvider.class);

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        LoginParamters loginParamters = (LoginParamters) authentication.getDetails();
        Object userName = loginParamters.get("userName");
        if (null == userDetails) {
            log.warn("{} 登录失败 用户不存在", userName);
            throw new BadCredentialsException("用户名或密码错误");
        }

        String password = (String) loginParamters.get("password");

        IUserService userService = SpringContextProvider.getBean(IUserService.class);
        // 验证密码
        boolean verifyResult = userService.verifyPassword(password, userDetails.getPassword());
        if (!verifyResult) {
            throw new BadCredentialsException("用户名或密码错误");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        IUserService userService = SpringContextProvider.getBean(IUserService.class);

        return userService.loadUserByUsername(username);
    }
}
