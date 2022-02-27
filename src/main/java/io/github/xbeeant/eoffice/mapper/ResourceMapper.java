package io.github.xbeeant.eoffice.mapper;

import com.github.pagehelper.Page;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.rest.vo.ResourcePerm;
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
     * @return {@link List}
     * @see List
     * @see Resource
     */
    List<Resource> folderResources(@Param("fid") Long fid);

    /**
     * has permission resources
     *
     * @param fid fid
     * @param uid uid
     * @param uid uid
     * @return {@link List}
     * @see List
     * @see Resource
     */
    List<Resource> hasPermissionResources(@Param("fid") Long fid, @Param("uid") String uid);

    /**
     * has permission resources
     *
     * @param keyWord 关键字
     * @param uid     uid
     * @param uid     uid
     * @return {@link List}
     * @see List
     * @see Resource
     */
    List<Resource> hasPermissionResourcesByKeyWord(@Param("keyWord") String keyWord, @Param("uid") String uid);

    /**
     * 已授权过的成员
     *
     * @param targetId 资源ID
     * @return {@link Page}
     * @see Page
     * @see ResourcePerm
     */
    Page<ResourcePerm> permed(@Param("targetId") Long targetId);
}
