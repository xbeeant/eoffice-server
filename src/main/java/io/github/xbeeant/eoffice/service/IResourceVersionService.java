package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.ResourceVersion;
import io.github.xbeeant.eoffice.rest.vo.ResourceVersionVo;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;

/**
 * @author mybatis code generator
 * @version Sun Dec 19 22:49:46 CST 2021
 */
public interface IResourceVersionService extends IMybatisPageHelperService<ResourceVersion, Long> {

    /**
     * fuzzy search vo by pager
     *
     * @param example 例子
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<ResourceVersionVo>> fuzzySearchVoByPager(ResourceVersion example, PageBounds pageBounds);
}
