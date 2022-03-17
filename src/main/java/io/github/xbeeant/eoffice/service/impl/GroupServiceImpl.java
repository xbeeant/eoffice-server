package io.github.xbeeant.eoffice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.GroupMapper;
import io.github.xbeeant.eoffice.model.Group;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IGroupService;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import io.github.xbeeant.spring.security.SecurityUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Thu Jan 13 09:18:55 CST 2022
 * eoffice_group
 */
@Service
public class GroupServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Group, Long> implements IGroupService {
    @Autowired
    private GroupMapper groupMapper;

    @Override
    public IMybatisPageHelperDao<Group, Long> getRepositoryDao() {
        return this.groupMapper;
    }

    @Override
    public void setDefaults(Group group) {
        if (group.getGid() == null) {
            group.setGid(IdWorker.getId());
        }
    }

    @Override
    public List<Group> treeList(Integer type) {
        // 若type=1表示取自建分组，还要检查是否create_by =自己的userId
        Long userId = null;
        if (type == 1) {
            SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();
            userId = Long.valueOf(userSecurityUser.getUserId());
        }
        return groupMapper.groups(type, userId);
    }

    @Override
    public ApiResponse<PageResponse<User>> users(Long gid, PageBounds pageBounds) {
        ApiResponse<PageResponse<User>> apiResponse = new ApiResponse<>();

        PageMethod.orderBy(pageBounds.getOrders());
        PageMethod.startPage(pageBounds.getPage(), pageBounds.getLimit());
        Page<User> result = groupMapper.users(gid);
        if (result.isEmpty()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }
        PageResponse<User> pageList = new PageResponse<>(result, result.getTotal(), pageBounds.getPage());
        apiResponse.setData(pageList);

        return apiResponse;
    }

    @Override
    public ApiResponse<PageResponse<User>> outoffGroupUsers(Long gid, User user, PageBounds pageBounds) {
        ApiResponse<PageResponse<User>> apiResponse = new ApiResponse<>();

        PageMethod.startPage(pageBounds.getPage(), pageBounds.getLimit());
        Page<User> result = groupMapper.outoffGroupUsers(user, gid);
        if (result.isEmpty()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }
        PageResponse<User> pageList = new PageResponse<>(result, result.getTotal(), pageBounds.getPage());
        apiResponse.setData(pageList);

        return apiResponse;
    }

    @Override
    public List<Long> parentIds(Long gid) {
        return groupMapper.parentIds(gid);
    }

    @Override
    public List<TreeNode> treeNodes(Integer type) {
        List<Group> list = treeList(type);

        List<TreeNode> treeNodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            // to TreeNode
            List<TreeNode> nodes = new ArrayList<>(list.size());
            TreeNode treeNode;
            for (Group group : list) {
                treeNode = new TreeNode();
                treeNode.setTitle(group.getName());
                treeNode.setKey(String.valueOf(group.getGid()));
                treeNode.setValue(String.valueOf(group.getGid()));
                treeNode.setpKey(String.valueOf(group.getPgid()));
                nodes.add(treeNode);
            }

            // 所有菜单构建父子分组项
            for (TreeNode topItem : nodes) {
                // 获取第一层分组
                for (TreeNode childItem : nodes) {
                    if (topItem.getKey().equals(childItem.getpKey())) {
                        topItem.addChildren(childItem);
                    }
                }
            }
            // 获取grade分组是1的分组
            for (TreeNode topMenu : nodes) {
                if ("0".equals(topMenu.getpKey())) {
                    treeNodes.add(topMenu);
                }
            }
        }

        return treeNodes;
    }

    @Override
    public ApiResponse<Group> updateByPrimaryKeySelective(Group item) {
        if (item.getGid().equals(item.getPgid())) {
            return new ApiResponse<>();
        }
        return super.updateByPrimaryKeySelective(item);
    }

    @Override
    public ApiResponse<Integer> deleteByPrimaryKey(Long keys) {
        ApiResponse<Integer> response = new ApiResponse<>();
        // 判断是否存在用户已经子群组
        // 用户判断
        PageMethod.startPage(1,1);
        Page<User> users = groupMapper.users(keys);
        if (null != users && !users.isEmpty()) {
            response.setResult(ErrorCodeConstant.CONFLICT, "该群组下还有用户，请先移除用户！");
            return response;
        }

        Group example = new Group();
        example.setPgid(keys);
        ApiResponse<Group> groupApiResponse = selectOneByExample(example);
        if (groupApiResponse.getSuccess()) {
            response.setResult(ErrorCodeConstant.CONFLICT, "该群组下还有群组，请先删除子群组！");
            return response;
        }

        return super.deleteByPrimaryKey(keys);
    }
}