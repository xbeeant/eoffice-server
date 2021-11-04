package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.http.Requests;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaobiao
 */
public class SessionInformationExpiredStrategyExt implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        HttpServletResponse response = event.getResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ApiResponse<String> api = new ApiResponse<>();
        api.setMsg("您的账号已经在别的地方登录，当前登录已失效。如果密码遭到泄露，请立即修改密码！");
        Requests.writeJson(response, api);
    }
}
