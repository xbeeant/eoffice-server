package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaobiao
 */
public class LogoutSuccessHandlerExt implements LogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutSuccessHandlerExt.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            SessionRegistry bean = SpringContextProvider.getBean(SessionRegistry.class);
            if (null != bean) {
                bean.removeSessionInformation(request.getSession().getId());
            }
        } catch (Exception e) {
            logger.warn("", e);
        }

        ApiResponse<String> result = new ApiResponse<>();
        result.setCode(0);
        // 返回json
        Requests.writeJson(response, result);
    }
}
