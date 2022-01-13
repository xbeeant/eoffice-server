package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:44:43 CST 2021
 */
@Mapper
public interface PermMapper extends IMybatisPageHelperDao<Perm, Long> {
    /**
     * 权益
     *
     * @param rid      rid
     * @param targetId targetId
     * @param type     类型
     * @return {@link Perm}
     * @see Perm
     */
    Perm perm(@Param("rid") Long rid, @Param("targetId") Long targetId, @Param("type") int type);

    /**
     * 用户
     *
     * @param rid rid
     * @return {@link List}
     * @see List
     * @see User
     */
    List<User> users(@Param("rid") Long rid);

    /**
     * 已存在的授权
     *
     * @param targetIds 用户ID列表
     * @param rid       rid
     * @return {@link List}
     * @see List
     * @see Perm
     */
    List<Perm> exists(@Param("targetIds") List<Long> targetIds, @Param("rid") Long rid);

    /**
     * remove exists
     *
     * @param targetIds     uid
     * @param rid rid
     * @return {@link Integer}
     * @see Integer
     */
    Integer removeExists(@Param("targetIds") List<Long> targetIds, @Param("rid") Long rid);
}
