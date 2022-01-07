package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;

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
}
