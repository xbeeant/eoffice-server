package io.github.xbeeant.eoffice.rest;

import com.github.pagehelper.Page;
import io.github.xbeeant.antdesign.TableResponse;
import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.service.IAbstractService;
import io.github.xbeeant.eoffice.model.Group;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.model.UserGroup;
import io.github.xbeeant.eoffice.service.IGroupService;
import io.github.xbeeant.eoffice.service.IUserGroupService;
import io.github.xbeeant.spring.mybatis.antdesign.PageRequest;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import io.github.xbeeant.spring.mybatis.rest.AbstractPagehelperRestFormController;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/1/13
 */
@RestController
@RequestMapping("api/team")
public class TeamRestController extends AbstractPagehelperRestFormController<Group, Long> {
    @Autowired
    private IGroupService groupService;

    @Autowired
    private IUserGroupService userGroupService;

    @Override
    public IAbstractService<Group, Long, PageBounds, PageResponse<Group>, Page<Group>> getService() {
        return groupService;
    }

    /**
     * 分组树形结构
     *
     * @param type 类型
     * @return {@link List}
     * @see List
     * @see TreeNode
     */
    @GetMapping("tree")
    public List<TreeNode> tree(Integer type) {
        List<Group> list = groupService.treeList(type);

        List<TreeNode> treeNodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            // to TreeNode
            List<TreeNode> nodes = new ArrayList<>(list.size());
            TreeNode treeNode;
            for (Group group : list) {
                treeNode = new TreeNode();
                treeNode.setTitle(group.getName());
                treeNode.setKey(group.getGid());
                treeNode.setpKey(group.getPgid());
                nodes.add(treeNode);
            }

            // 所有菜单构建父子菜单项
            for (TreeNode topItem : nodes) {
                // 获取第一层菜单
                for (TreeNode childItem : nodes) {
                    if (topItem.getKey().equals(childItem.getpKey())) {
                        topItem.addChildren(childItem);
                    }
                }
            }
            // 获取grade菜单是1的菜单
            for (TreeNode topMenu : nodes) {
                if (0 == topMenu.getpKey()) {
                    treeNodes.add(topMenu);
                }
            }
        }

        return treeNodes;
    }

    /**
     * 用户
     *
     * @param gid gid
     * @param pageRequest pageRequest
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see TableResponse
     */
    @GetMapping("user")
    public ApiResponse<TableResponse<User>> user(Long gid, PageRequest pageRequest) {
        ApiResponse<TableResponse<User>> result = new ApiResponse<>();
        // 通过rid查询所有的历史版本 分页
        ApiResponse<PageResponse<User>> pageResponseApiResponse = groupService.users(gid, pageRequest.getPageBounds());
        PageResponse<User> list = pageResponseApiResponse.getData();
        if (!pageResponseApiResponse.getSuccess()) {
            result.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return result;
        }
        TableResponse<User> pageResponse = new TableResponse<>();
        pageResponse.setList(list);
        pageResponse.setPagination(list.getPagination());
        result.setData(pageResponse);
        return result;
    }

    @DeleteMapping("user")
    public ApiResponse<Integer> user(Long gid, Long uid) {
        UserGroup userGroup = new UserGroup();
        userGroup.setUid(uid);
        userGroup.setGid(gid);
        return userGroupService.deleteByExample(userGroup);
    }

    @GetMapping("user/unlink")
    public ApiResponse<TableResponse<User>> table(
            Long gid,
            User user,
            @ApiParam(value = "页面要求", required = true) PageRequest pageRequest) {
        ApiResponse<TableResponse<User>> apiResponse = new ApiResponse<>();
        PageRequest webRequest = new PageRequest(pageRequest);

        ApiResponse<PageResponse<User>> list = groupService.outoffGroupUsers(gid, user, webRequest.getPageBounds());
        if (!list.getSuccess()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }

        TableResponse<User> pageResponse = new TableResponse<>();
        pageResponse.setList(list.getData());
        pageResponse.setPagination(list.getData().getPagination());
        apiResponse.setData(pageResponse);
        return apiResponse;
    }

    @PostMapping("user")
    public ApiResponse<UserGroup> groupUserAdd(Long gid, Long userId) {
        UserGroup example = new UserGroup();
        example.setGid(gid);
        example.setUid(userId);
        ApiResponse<UserGroup> existResponse = userGroupService.selectOneByExample(example);
        if (!existResponse.getSuccess()) {

            UserGroup userGroup = new UserGroup();
            userGroup.setUid(userId);
            userGroup.setGid(gid);
            return userGroupService.insertSelective(userGroup);
        }

        ApiResponse<UserGroup> result = new ApiResponse<>();
        result.setData(existResponse.getData());
        return result;
    }
}
