package io.github.xbeeant.eoffice.mapper;

import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 父节点
     *
     * @param fid 目录ID
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> parents(Long fid);

    /**
     * update size
     *
     * @param fids 目录ID
     * @param size 大小
     * @return {@link Integer}
     * @see Integer
     */
    Integer increaseSize(@Param("fids") List<Long> fids, @Param("size") Long size);

    /**
     * decrease size
     *
     * @param fids 目录ID
     * @param size 大小
     * @return {@link Integer}
     * @see Integer
     */
    Integer decreaseSize(@Param("fids") List<Long> fids, @Param("size") Long size);

    /**
     * update size
     *
     * @param fids    目录ID
     * @param oldSize oldSize
     * @param newSize newSize
     * @return {@link Integer}
     * @see Integer
     */
    Integer updateResourceSize(@Param("fids") List<Long> fids, @Param("oldSize") Long oldSize, @Param("newSize") Long newSize);
}