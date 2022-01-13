package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Group;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Thu Jan 13 09:18:55 CST 2022
 */
public interface IGroupService extends IMybatisPageHelperService<Group, Long> {

    /**
     * tree list
     *
     * @param type 类型
     * @return {@link List}
     * @see List
     * @see Group
     */
    List<Group> treeList(Integer type);

    /**
     * 用户
     *
     * @param gid gid
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<User>> users(Long gid, PageBounds pageBounds);

    /**
     * outoff group users
     *
     * @param gid gid
     * @param user 用户
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<User>> outoffGroupUsers(Long gid, User user, PageBounds pageBounds);
}