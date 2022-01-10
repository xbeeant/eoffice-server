package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 17:59:08 CST 2021
 */
@Mapper
public interface UserMapper extends IMybatisPageHelperDao<User, Long> {
    /**
     * 搜索
     *
     * @param search 搜索
     * @return {@link List}
     * @see List
     * @see User
     */
    List<User> search(String search);
}