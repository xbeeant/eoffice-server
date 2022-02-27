package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaobiao
 * @version 2021/7/8
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityUser<User> userSecurityUser = SecurityHelper.tokenToUser(request);
        if (null != userSecurityUser) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userSecurityUser, null, userSecurityUser.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
