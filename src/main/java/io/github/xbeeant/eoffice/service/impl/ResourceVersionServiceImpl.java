package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ResourceVersionMapper;
import io.github.xbeeant.eoffice.model.ResourceVersion;
import io.github.xbeeant.eoffice.service.IResourceVersionService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * eoffice_resource_version
 */
@Service
public class ResourceVersionServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<ResourceVersion, Long> implements IResourceVersionService {
    @Autowired
    private ResourceVersionMapper resourceVersionMapper;

    @Override
    public IMybatisPageHelperDao<ResourceVersion, Long> getRepositoryDao() {
        return this.resourceVersionMapper;
    }

    @Override
    public void setDefaults(ResourceVersion resourceVersion) {
        if (resourceVersion.getVid() == null) {
            resourceVersion.setVid(IdWorker.getId());
        }
    }
}