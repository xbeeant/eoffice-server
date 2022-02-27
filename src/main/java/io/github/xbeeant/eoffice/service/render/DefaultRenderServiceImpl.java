package io.github.xbeeant.eoffice.service.render;

import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.stereotype.Component;

/**
 * @author xiaobiao
 * @version 2022/2/12
 */
@Component
public class DefaultRenderServiceImpl implements AbstractRenderService {
    @Override
    public void setExtra(ResourceVo resource, String mode, SecurityUser<User> userSecurityUser) {

    }
}
