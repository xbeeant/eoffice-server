package io.github.xbeeant.eoffice.rest;

import com.github.pagehelper.Page;
import io.github.xbeeant.core.service.IAbstractService;
import io.github.xbeeant.eoffice.model.DocTemplateCategory;
import io.github.xbeeant.eoffice.service.IDocTemplateCategoryService;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import io.github.xbeeant.spring.mybatis.rest.AbstractPagehelperRestFormController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaobiao
 * @version 2022/1/19
 */
@Api(tags = "模板分类模块")
@RestController
@RequestMapping("api/template/category")
public class TemplateCategoryRestController extends AbstractPagehelperRestFormController<DocTemplateCategory, Long> {
    @Autowired
    private IDocTemplateCategoryService templateCategoryService;

    @Override
    public IAbstractService<DocTemplateCategory, Long, PageBounds, PageResponse<DocTemplateCategory>, Page<DocTemplateCategory>> getService() {
        return templateCategoryService;
    }
}
