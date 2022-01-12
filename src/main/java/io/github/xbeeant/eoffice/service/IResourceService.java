package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.model.User;
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
     * @param fid  目录ID
     * @param uid  用户ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    ApiResponse<Resource> upload(MultipartFile file, Long fid, String uid);

    /**
     * has permission resources
     *
     * @param fid        fid
     * @param uid        uid
     * @param pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    ApiResponse<List<Resource>> hasPermissionResources(Long fid, String uid, PageBounds pageBounds);

    /**
     * 详情
     *
     * @param rid 资源ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    ApiResponse<ResourceVo> detail(Long rid);

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
    ApiResponse<String> save(Long rid, String value, String uid);

    /**
     * 权限
     *
     * @param rid    资源ID
     * @param userId 用户标识
     */
    Perm permission(Long rid, Long userId);

    /**
     * 添加/创建资源
     *
     * @param type 类型
     * @param fid  目录ID
     * @param uid  用户标识
     * @return {@link Storage}
     * @see Storage
     */
    ResourceVo add(String type, Long fid, String uid);

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
     * @param users   用户
     * @param perm    权限
     * @param rid     资源ID
     * @param actorId actorId
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    ApiResponse<String> perm(List<Long> users, List<String> perm, Long rid);

    /**
     * 烫
     *
     * @param rid 掉
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<ResourcePerm>> permed(Long rid, PageBounds pageBounds);
}
