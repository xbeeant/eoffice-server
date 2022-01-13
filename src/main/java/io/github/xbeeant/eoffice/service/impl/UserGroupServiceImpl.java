package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.UserGroupMapper;
import io.github.xbeeant.eoffice.model.UserGroup;
import io.github.xbeeant.eoffice.service.IUserGroupService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Thu Jan 13 09:19:05 CST 2022
 * eoffice_user_group
 */
@Service
public class UserGroupServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<UserGroup, Long> implements IUserGroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;

    @Override
    public IMybatisPageHelperDao<UserGroup, Long> getRepositoryDao() {
        return this.userGroupMapper;
    }

    @Override
    public void setDefaults(UserGroup usergroup) {
        if (usergroup.getId() == null) {
            usergroup.setId(IdWorker.getId());
        }
    }
}