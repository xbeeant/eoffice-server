package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.DocTemplateCategoryMapper;
import io.github.xbeeant.eoffice.model.DocTemplate;
import io.github.xbeeant.eoffice.model.DocTemplateCategory;
import io.github.xbeeant.eoffice.service.IDocTemplateCategoryService;
import io.github.xbeeant.eoffice.service.IDocTemplateService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Wed Jan 19 10:09:09 CST 2022
 * eoffice_doc_template_category
 */
@Service
public class DocTemplateCategoryServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<DocTemplateCategory, Long> implements IDocTemplateCategoryService {
    @Autowired
    private DocTemplateCategoryMapper docTemplateCategoryMapper;

    @Autowired
    private IDocTemplateService docTemplateService;

    @Override
    public IMybatisPageHelperDao<DocTemplateCategory, Long> getRepositoryDao() {
        return this.docTemplateCategoryMapper;
    }

    @Override
    public void setDefaults(DocTemplateCategory doctemplatecategory) {
        if (doctemplatecategory.getCid() == null) {
            doctemplatecategory.setCid(IdWorker.getId());
        }
    }

    @Override
    public List<TreeNode> treeList(String type) {
        List<DocTemplateCategory> categories;
        if (null != type) {
            categories = docTemplateCategoryMapper.fuzzySearchType("%" + type + "%");
        } else {
            ApiResponse<List<DocTemplateCategory>> listApiResponse = selectAllByExample(new DocTemplateCategory());
            categories = listApiResponse.getData();
        }

        List<TreeNode> treeNodes = new ArrayList<>(categories.size());
        for (DocTemplateCategory category : categories) {
            TreeNode treeNode;
            treeNode = new TreeNode();
            treeNode.setTitle(category.getName());
            treeNode.setKey(String.valueOf(category.getCid()));
            treeNode.setpKey(String.valueOf(category.getPcid()));
            treeNode.setValue(treeNode.getKey());
            treeNode.setDisabled(true);
            treeNodes.add(treeNode);
        }

        // 遍历每个分类下的模板
        for (TreeNode treeNode : treeNodes) {
            DocTemplate docTemplate = new DocTemplate();
            docTemplate.setCid(Long.valueOf(treeNode.getKey()));
            ApiResponse<List<DocTemplate>> listApiResponse = docTemplateService.selectAllByExample(docTemplate);
            if (listApiResponse.getSuccess()) {
                List<DocTemplate> data = listApiResponse.getData();
                for (DocTemplate template : data) {
                    TreeNode subNode = new TreeNode();
                    subNode.setTitle(template.getTitle());
                    subNode.setKey(String.valueOf(template.getTid()));
                    subNode.setpKey(treeNode.getKey());
                    subNode.setValue(subNode.getKey());
                    treeNode.addChildren(subNode);
                }
            }
        }

        return treeNodes;
    }
}