package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.Config;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mybatis code generator
 * @version Mon Nov 22 22:00:22 CST 2021
 */
@Mapper
public interface ConfigMapper extends IMybatisPageHelperDao<Config, Long> {
    /**
     * value of
     *
     * @param module 模块
     * @param ckey   ckey
     * @return {@link String}
     * @see String
     */
    String valueOf(@Param("module") String module, @Param("ckey") String ckey);
}