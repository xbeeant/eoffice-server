package io.github.xbeeant.eoffice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ResourceVersionMapper;
import io.github.xbeeant.eoffice.model.ResourceVersion;
import io.github.xbeeant.eoffice.rest.vo.ResourceVersionVo;
import io.github.xbeeant.eoffice.service.IResourceVersionService;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
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

    @Override
    public ApiResponse<PageResponse<ResourceVersionVo>> fuzzySearchVoByPager(ResourceVersion example, PageBounds pageBounds) {
        ApiResponse<PageResponse<ResourceVersionVo>> apiResponse = new ApiResponse<>();

        PageMethod.orderBy("create_at desc");
        PageMethod.startPage(pageBounds.getPage(), pageBounds.getLimit());
        Page<ResourceVersionVo> result = resourceVersionMapper.fuzzySearchVo(example);
        if (result.isEmpty()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }
        PageResponse<ResourceVersionVo> pageList = new PageResponse<>(result, result.getTotal(), pageBounds.getPage());
        apiResponse.setData(pageList);

        return apiResponse;
    }
}