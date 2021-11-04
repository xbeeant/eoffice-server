package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:17:38 CST 2021
 */
@Mapper
public interface ResourceMapper extends IMybatisPageHelperDao<Resource, Long> {
    /**
     * has permission resources
     *
     * @param fid fid
     * @param uid             uid
     * @return {@link List}
     * @see List
     * @see Resource
     */
    List<Resource> hasPermissionResources(@Param("fid") Long fid, @Param("uid") String uid);
}
