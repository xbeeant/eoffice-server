package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.UserLogMapper;
import io.github.xbeeant.eoffice.model.UserLog;
import io.github.xbeeant.eoffice.service.IUserLogService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xbeeant mybatis generator
 * @version Wed Mar 02 18:25:54 CST 2022
 * eoffice_user_log
 */
@Service
public class UserLogServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<UserLog, Long> implements IUserLogService {
    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public boolean isWithBlobs() {
        return true;
    }

    @Override
    public IMybatisPageHelperDao<UserLog, Long> getRepositoryDao() {
        return this.userLogMapper;
    }

    @Override
    public void setDefaults(UserLog userlog) {
        if (userlog.getId() == null) {
            userlog.setId(IdWorker.getId());
        }
    }
}