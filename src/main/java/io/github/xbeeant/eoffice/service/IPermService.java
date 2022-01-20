package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:44:43 CST 2021
 */
public interface IPermService extends IMybatisPageHelperService<Perm, Long> {
    /**
     * 资源权益
     *
     * @param targetId 目标对象ID
     * @param rid      资源ID
     * @return {@link Perm}
     * @see Perm
     */
    Perm perm(Long targetId, Long rid);

    /**
     * share perm
     *
     * @param userId 用户标识
     * @param rid    掉
     * @param share  分享
     * @return {@link Perm}
     * @see Perm
     */
    Perm sharePerm(Long userId, Long rid, Share share);

    /**
     * 用户
     *
     * @param uid uid
     * @return {@link List}
     * @see List
     * @see User
     */
    List<User> users(Long uid);

    /**
     * 授权
     *
     * @param targetIds  目标ID
     * @param perm       权限
     * @param resource   资源
     * @param type       类型
     * @param targetType 目标类型
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    ApiResponse<String> perm(List<Long> targetIds, List<String> perm, Resource resource, PermType type, PermTargetType targetType);

}
