package io.github.xbeeant.eoffice.rest;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.service.IAbstractService;
import io.github.xbeeant.eoffice.aspect.annotation.ResourceOwner;
import io.github.xbeeant.eoffice.model.DocTemplate;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.service.IDocTemplateCategoryService;
import io.github.xbeeant.eoffice.service.IDocTemplateService;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import io.github.xbeeant.spring.mybatis.rest.AbstractPagehelperRestFormController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/1/19
 */
@RestController
@RequestMapping("api/template")
public class TemplateRestController extends AbstractPagehelperRestFormController<DocTemplate, Long> {
    @Autowired
    private IDocTemplateService docTemplateService;

    @Autowired
    private IDocTemplateCategoryService docTemplateCategoryService;

    @Override
    public IAbstractService<DocTemplate, Long, PageBounds, PageResponse<DocTemplate>, Page<DocTemplate>> getService() {
        return docTemplateService;
    }

    @Override
    public ApiResponse<DocTemplate> post(DocTemplate record, HttpServletRequest request, HttpServletResponse response) {
        String files = request.getParameter("files");
        List<Storage> storages = JSON.parseArray(files, Storage.class);
        for (Storage storage : storages) {
            DocTemplate docTemplate = new DocTemplate();
            docTemplate.setCid(record.getCid());
            docTemplate.setSid(storage.valueOfKey());
            docTemplate.setTitle(storage.getName());
            docTemplateService.insertSelective(docTemplate);
        }

        return new ApiResponse<>();
    }

    @Override
    @ResourceOwner(id = "id", selectService = IDocTemplateService.class)
    public ApiResponse<Integer> delete(@PathVariable(name = "id") Long id, HttpServletRequest request, HttpServletResponse response) {
        return super.delete(id, request, response);
    }

    /**
     * 分组树形结构
     *
     * @param type 文件后缀
     * @return {@link List}
     * @see List
     * @see TreeNode
     */
    @GetMapping("tree")
    public List<TreeNode> tree(String type) {
        return docTemplateCategoryService.treeList(type);
    }
}
