package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 17:59:08 CST 2021
 */
@Mapper
public interface UserMapper extends IMybatisPageHelperDao<User, Long> {
}