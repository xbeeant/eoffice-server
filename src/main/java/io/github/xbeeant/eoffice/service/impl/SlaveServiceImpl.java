package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.SlaveMapper;
import io.github.xbeeant.eoffice.model.Slave;
import io.github.xbeeant.eoffice.service.ISlaveService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Mon Feb 21 19:14:17 CST 2022
 * eoffice_slave
 */
@Service
public class SlaveServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Slave, Long> implements ISlaveService {
    @Autowired
    private SlaveMapper slaveMapper;

    @Override
    public IMybatisPageHelperDao<Slave, Long> getRepositoryDao() {
        return this.slaveMapper;
    }

    @Override
    public void setDefaults(Slave slave) {
        if (slave.getId() == null) {
            slave.setId(IdWorker.getId());
        }
    }
}