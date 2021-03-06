package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.rest.vo.ResourcePerm;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:17:38 CST 2021
 */
public interface IResourceService extends IMybatisPageHelperService<Resource, Long> {

    /**
     * 上传资源
     *
     * @param file 文件
     * @param uid  用户ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    ApiResponse<Storage> upload(MultipartFile file, String uid);

    /**
     * save resource
     *
     * @param fid      目录ID
     * @param uid      uid
     * @param filename 文件名
     * @param storage  存储
     * @return {@link ResourceVo}
     * @see ResourceVo
     */
    ResourceVo saveResource(Long fid, String uid, String filename, Storage storage);

    /**
     * has permission resources
     *
     * @param fid        fid
     * @param uid        uid
     * @param pageBounds 分页参数
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    ApiResponse<List<Resource>> hasPermissionResources(Long fid, String uid, PageBounds pageBounds);

    /**
     * has permission resources
     *
     * @param keyWords   关键词
     * @param uid        uid
     * @param pageBounds 分页参数
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    ApiResponse<List<Resource>> hasPermissionResources(String keyWords, String uid, PageBounds pageBounds);

    /**
     * 详情
     *
     * @param rid 资源ID
     * @param vid 版本ID，可为空，空为最新版本
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    ApiResponse<ResourceVo> detail(Long rid, Long vid);

    /**
     * 下载
     *
     * @param rid      资源ID
     * @param sid      附件ID
     * @param request  请求
     * @param response 响应
     */
    void download(Long rid, Long sid, HttpServletRequest request, HttpServletResponse response);

    /**
     * download attachment
     *
     * @param rid      资源ID
     * @param aid      附件ID
     * @param request  请求
     * @param response 响应
     */
    void downloadAttachment(Long rid, Long aid, HttpServletRequest request, HttpServletResponse response);

    /**
     * 保存
     *
     * @param rid   资源ID
     * @param value 值
     * @param uid   操作人ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    ApiResponse<Resource> save(Long rid, String value, String uid);


    /**
     * 权限
     *
     * @param rid    资源ID
     * @param userId 用户标识
     * @return {@link Perm}
     * @see Perm
     */
    Perm permission(Long rid, Long userId);


    /**
     * share permission
     *
     * @param rid    掉
     * @param userId 用户标识
     * @return {@link Perm}
     * @see Perm
     */
    Perm sharePermission(Long rid, Long userId, Share share);

    /**
     * 添加/创建资源
     *
     * @param type 类型
     * @param name
     * @param fid  目录ID
     * @param cid
     * @param uid  用户标识
     * @return {@link Storage}
     * @see Storage
     */
    ResourceVo add(String type, String name, Long fid, Long cid, String uid);

    /**
     * 用户
     *
     * @param rid 资源ID
     * @return {@link List}
     * @see List
     * @see User
     */
    List<User> users(Long rid);

    /**
     * 授权
     *
     * @param targetId   用户
     * @param perm       权限
     * @param targetType 目标类型
     * @param rid        资源ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    ApiResponse<String> perm(List<Long> targetId, List<String> perm, PermTargetType targetType, Long rid);

    /**
     * 已授权的资源列表
     *
     * @param rid        资源ID
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<ResourcePerm>> permed(Long rid, PageBounds pageBounds);

    /**
     * 移动
     *
     * @param rids    rid
     * @param fid     文件ID
     * @param fromFid fromFid
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Integer
     */
    ApiResponse<Integer> move(List<Long> rids, Long fid, Long fromFid);

    /**
     * overwrite resource
     *
     * @param rid     资源ID
     * @param userId  用户标识
     * @param storage 存储
     * @return {@link ResourceVo}
     * @see ResourceVo
     */
    ResourceVo overwriteResource(Long rid, String userId, Storage storage);
}
