package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:44:43 CST 2021
 */
@Mapper
public interface PermMapper extends IMybatisPageHelperDao<Perm, Long> {
    /**
     * 权益
     *
     * @param targetId targetId
     * @param uid      uid
     * @param type     类型
     * @return {@link Perm}
     * @see Perm
     */
    Perm perm(@Param("targetId") Long targetId, @Param("uid") Long uid, @Param("type") int type);
}
