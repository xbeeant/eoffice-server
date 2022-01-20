package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.DocTemplateMapper;
import io.github.xbeeant.eoffice.model.DocTemplate;
import io.github.xbeeant.eoffice.service.IDocTemplateService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Wed Jan 19 10:09:16 CST 2022
 * eoffice_doc_template
 */
@Service
public class DocTemplateServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<DocTemplate, Long> implements IDocTemplateService {
    @Autowired
    private DocTemplateMapper docTemplateMapper;

    @Override
    public IMybatisPageHelperDao<DocTemplate, Long> getRepositoryDao() {
        return this.docTemplateMapper;
    }

    @Override
    public void setDefaults(DocTemplate doctemplate) {
        if (doctemplate.getTid() == null) {
            doctemplate.setTid(IdWorker.getId());
        }
    }
}