package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.security.SecurityUser;

import java.util.List;

/**
 * @author mybatis code generator
 * @version Sat Oct 30 17:59:08 CST 2021
 */
public interface IUserService extends IMybatisPageHelperService<User, Long> {
    /**
     * load user by username
     *
     * @param username 用户名
     * @return {@link SecurityUser}
     * @see SecurityUser
     * @see User
     */
    SecurityUser<User> loadUserByUsername(String username);

    /**
     * verify password
     *
     * @param rawPassword rawPassword
     * @param encodedPassword encodedPassword
     * @return {@link boolean}
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 搜索
     *
     * @param search 搜索
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    ApiResponse<List<User>> search(String search);
}