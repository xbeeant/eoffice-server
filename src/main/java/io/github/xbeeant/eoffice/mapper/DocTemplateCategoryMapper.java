package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.DocTemplateCategory;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Wed Jan 19 10:09:09 CST 2022
 */
@Mapper
public interface DocTemplateCategoryMapper extends IMybatisPageHelperDao<DocTemplateCategory, Long> {
    /**
     * fuzzy search type
     *
     * @param type 类型
     * @return {@link List}
     * @see List
     * @see DocTemplateCategory
     */
    List<DocTemplateCategory> fuzzySearchType(String type);
}