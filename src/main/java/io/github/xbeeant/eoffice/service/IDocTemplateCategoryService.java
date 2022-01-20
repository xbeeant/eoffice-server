package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.eoffice.model.DocTemplateCategory;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Wed Jan 19 10:09:09 CST 2022
 */
public interface IDocTemplateCategoryService extends IMybatisPageHelperService<DocTemplateCategory, Long> {
    /**
     * tree list
     *
     * @param type 文件类型
     * @return {@link List}
     * @see List
     * @see TreeNode
     */
    List<TreeNode> treeList(String type);
}