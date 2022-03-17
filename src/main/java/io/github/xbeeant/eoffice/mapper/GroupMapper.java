package io.github.xbeeant.eoffice.mapper;

import com.github.pagehelper.Page;
import io.github.xbeeant.eoffice.model.Group;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Thu Jan 13 09:18:55 CST 2022
 */
@Mapper
public interface GroupMapper extends IMybatisPageHelperDao<Group, Long> {

    /**
     * 组
     *
     * @param type   类型
     * @param userId 用户标识
     * @return {@link List}
     * @see List
     * @see Group
     */
    List<Group> groups(@Param("type") Integer type, @Param("userId") Long userId);

    /**
     * 用户
     *
     * @param gid gid
     * @return {@link Page}
     * @see Page
     * @see User
     */
    Page<User> users(Long gid);

    /**
     * outoff group users
     *
     * @param user 用户
     * @param gid  gid
     * @return {@link Page}
     * @see Page
     * @see User
     */
    Page<User> outoffGroupUsers(@Param("user") User user, @Param("gid") Long gid);

    /**
     * parent ids
     *
     * @param gid gid
     * @return {@link List}
     * @see List
     * @see Long
     */
    List<Long> parentIds(Long gid);
}