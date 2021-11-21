package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:17:38 CST 2021
 */
public interface IResourceService extends IMybatisPageHelperService<Resource, Long> {

    /**
     * 插入
     *
     * @param file 文件
     * @param fid  目录ID
     * @param uid  用户ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    ApiResponse<Resource> insert(MultipartFile file, Long fid, String uid);

    /**
     * has permission resources
     *
     * @param fid fid
     * @param uid uid
     * @param pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    ApiResponse<List<Resource>> hasPermissionResources(Long fid, String uid, PageBounds pageBounds);
}
