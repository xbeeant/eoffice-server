package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.antdesign.MenuItem;
import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 18:23:36 CST 2021
 */
public interface IFolderService extends IMybatisPageHelperService<Folder, Long> {
    /**
     * has permission folders
     *
     * @param userId 用户标识
     * @return {@link List}
     * @see List
     * @see MenuItem
     */
    List<TreeNode> hasPermissionFolders(String userId);

    /**
     * has permission folders
     *
     * @param userId 用户标识
     * @return {@link List}
     * @see List
     * @see MenuItem
     */
    List<MenuItem> hasPermissionMenus(String userId);
    /**
     * 所有父节点
     *
     * @param fid 目录ID
     * @return {@link List}
     * @see List
     * @see Folder
     */
    MenuItem menuItem(Long fid);

    /**
     * 面包屑
     *
     * @param fid 文件夹ID
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> breadcrumb(Long fid);

    /**
     * update size
     *
     * @param fid 目录ID
     * @param size 大小
     */
    void updateFolderSize(Long fid, Long size);

    /**
     * sub folders
     *
     * @param fid 支撑材
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> subFolders(Long fid);

    /**
     * update size
     *
     * @param fid 支撑材
     * @param resourceOldSize oldSize
     * @param resourceNewSize newSize
     */
    void updateFolderAndResourceSize(Long fid, Long resourceOldSize, Long resourceNewSize);

    /**
     * parent folders
     *
     * @param fid 支撑材
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> parentFolders(Long fid);
}