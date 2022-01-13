package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.User;
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
     * @param type     类型
     * @return {@link Perm}
     * @see Perm
     */
    Perm perm(Long targetId, Long rid, PermType type);

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
     * @param targetIds 目标ID
     * @param perm      权限
     * @param rid       资源ID
     * @param type      类型
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    ApiResponse<String> perm(List<Long> targetIds, List<String> perm, Long rid, PermType type);
}
