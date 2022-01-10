package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.User;
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
     * @param uid      用户ID
     * @param type     类型
     * @return {@link Perm}
     * @see Perm
     */
    Perm perm(Long targetId, Long uid, int type);

    /**
     * 用户
     *
     * @param targetId targetId
     * @return {@link List}
     * @see List
     * @see User
     */
    List<User> users(Long targetId);

    /**
     * 授权
     *
     * @param users    用户
     * @param perm     权限
     * @param targetId 目标ID
     * @param type     类型
     * @param actorId  actorId
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    ApiResponse<String> perm(List<Long> users, List<String> perm, Long targetId, Integer type, String actorId);
}
