package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:23:36 CST 2021
 */
@Mapper
public interface FolderMapper extends IMybatisPageHelperDao<Folder, Long> {
    /**
     * has permission folders
     *
     * @param userId 用户标识
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> hasPermissionFolders(String userId);

    /**
     * sub folders
     *
     * @param fid 目录ID
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> subFolders(Long fid);
}