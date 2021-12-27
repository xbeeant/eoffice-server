package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.antdesign.MenuItem;
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
    List<MenuItem> hasPermissionFolders(String userId);

    /**
     * 所有父节点
     *
     * @param fid 支撑材
     * @return {@link List}
     * @see List
     * @see Folder
     */
    MenuItem parents(Long fid);

    /**
     * 面包屑
     *
     * @param fid 文件夹ID
     * @return {@link List}
     * @see List
     * @see Folder
     */
    List<Folder> breadcrumb(Long fid);
}