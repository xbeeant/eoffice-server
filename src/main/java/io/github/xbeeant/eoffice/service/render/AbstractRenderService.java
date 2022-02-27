package io.github.xbeeant.eoffice.service.render;

import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.spring.security.SecurityUser;

/**
 * @author xiaobiao
 * @version 2022/2/12
 */
public interface AbstractRenderService {
    /**
     * set extra
     *  @param resource 资源
     * @param mode
     * @param userSecurityUser
     */
    void setExtra(ResourceVo resource, String mode, SecurityUser<User> userSecurityUser);
}
