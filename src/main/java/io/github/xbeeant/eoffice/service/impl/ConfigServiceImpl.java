package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ConfigMapper;
import io.github.xbeeant.eoffice.model.Config;
import io.github.xbeeant.eoffice.service.IConfigService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * eoffice_config
 */
@Service
public class ConfigServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Config, Long> implements IConfigService {
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public IMybatisPageHelperDao<Config, Long> getRepositoryDao() {
        return this.configMapper;
    }

    @Override
    public void setDefaults(Config record) {
        if (record.getCid() == null) {
            record.setCid(IdWorker.getId());
        }
    }

    @Override
    public String valueOf(String module, String ckey) {
        return configMapper.valueOf(module, ckey);
    }
}