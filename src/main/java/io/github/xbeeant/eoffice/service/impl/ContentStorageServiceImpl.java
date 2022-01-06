package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ContentStorageMapper;
import io.github.xbeeant.eoffice.model.ContentStorage;
import io.github.xbeeant.eoffice.service.IContentStorageService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * eoffice_content_storage
 */
@Service
public class ContentStorageServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<ContentStorage, Long> implements IContentStorageService {

    @Autowired
    private ContentStorageMapper storageMapper;

    @Override
    public IMybatisPageHelperDao<ContentStorage, Long> getRepositoryDao() {
        return this.storageMapper;
    }

    @Override
    public void setDefaults(ContentStorage record) {
        if (record.getSid() == null) {
            record.setSid(IdWorker.getId());
        }
    }

}
