package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.eoffice.model.UserGroup;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;

import java.util.Set;

/**
 * @author mybatis code generator
 * @version Thu Jan 13 09:19:05 CST 2022
 */
public interface IUserGroupService extends IMybatisPageHelperService<UserGroup, Long> {
    Set<Long> userGroupIds(Long targetId);
}