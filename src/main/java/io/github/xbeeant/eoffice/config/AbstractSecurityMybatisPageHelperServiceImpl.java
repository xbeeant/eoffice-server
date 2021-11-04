package io.github.xbeeant.eoffice.config;

import io.github.xbeeant.core.BaseModelObject;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.mybatis.pagehelper.AbstractMybatisPageHelperServiceImpl;
import io.github.xbeeant.spring.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaobiao
 * @version 2021/10/29
 */
public abstract class AbstractSecurityMybatisPageHelperServiceImpl<T extends BaseModelObject<K>, K> extends AbstractMybatisPageHelperServiceImpl<T, K> {

    private static final Logger log = LoggerFactory.getLogger(AbstractSecurityMybatisPageHelperServiceImpl.class);

    @Override
    public String getActorId(T record) {
        if (null != record.getUpdateBy()) {
            return record.getUpdateBy();
        }
        try {
            SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();
            return userSecurityUser.getUserId();
        } catch (Exception e) {
            return "";
        }
    }
}
