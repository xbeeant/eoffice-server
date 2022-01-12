package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ShareRangeMapper;
import io.github.xbeeant.eoffice.model.ShareRange;
import io.github.xbeeant.eoffice.service.IShareRangeService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Wed Jan 12 11:56:41 CST 2022
 * eoffice_share_range
 */
@Service
public class ShareRangeServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<ShareRange, Long> implements IShareRangeService {
    @Autowired
    private ShareRangeMapper shareRangeMapper;

    @Override
    public IMybatisPageHelperDao<ShareRange, Long> getRepositoryDao() {
        return this.shareRangeMapper;
    }

    @Override
    public void setDefaults(ShareRange sharerange) {
        if (sharerange.getShareId() == null) {
            sharerange.setShareId(IdWorker.getId());
        }
    }
}