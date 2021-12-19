package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ResourceAttachmentMapper;
import io.github.xbeeant.eoffice.model.ResourceAttachment;
import io.github.xbeeant.eoffice.service.IResourceAttachmentService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * eoffice_resource_attachment
 */
@Service
public class ResourceAttachmentServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<ResourceAttachment, Long> implements IResourceAttachmentService {
    @Autowired
    private ResourceAttachmentMapper resourceAttachmentMapper;

    @Override
    public IMybatisPageHelperDao<ResourceAttachment, Long> getRepositoryDao() {
        return this.resourceAttachmentMapper;
    }

    @Override
    public void setDefaults(ResourceAttachment record) {
        if (record.getAid() == null) {
            record.setAid(IdWorker.getId());
        }
    }
}