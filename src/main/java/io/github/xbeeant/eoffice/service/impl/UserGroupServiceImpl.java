package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.UserGroupMapper;
import io.github.xbeeant.eoffice.model.UserGroup;
import io.github.xbeeant.eoffice.service.IGroupService;
import io.github.xbeeant.eoffice.service.IUserGroupService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Thu Jan 13 09:19:05 CST 2022
 * eoffice_user_group
 */
@Service
public class UserGroupServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<UserGroup, Long> implements IUserGroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private IGroupService groupService;

    @Override
    public IMybatisPageHelperDao<UserGroup, Long> getRepositoryDao() {
        return this.userGroupMapper;
    }

    @Override
    public void setDefaults(UserGroup usergroup) {
        if (usergroup.getId() == null) {
            usergroup.setId(IdWorker.getId());
        }
    }

    @Override
    public Set<Long> userGroupIds(Long targetId) {
        // 获取用户所在分组
        UserGroup userGroupExample = new UserGroup();
        userGroupExample.setUid(Long.valueOf(targetId));
        ApiResponse<List<UserGroup>> listApiResponse = selectAllByExample(userGroupExample);
        Set<Long> gids = new HashSet<>();
        if (listApiResponse.getSuccess()) {
            for (UserGroup item : listApiResponse.getData()) {
                List<Long> ids = groupService.parentIds(item.getGid());
                gids.addAll(ids);
            }
        }
        return gids;
    }

    @Override
    public ApiResponse<List<UserGroup>> selectAllByExample(UserGroup example) {
        List<String> orders = new ArrayList<>();
        return selectAllByExample(example, orders);
    }
}