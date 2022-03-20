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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/1/13
 */
@Api(tags = "群组模块")
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
    @ApiOperation(value = "群组树列表")
    public List<TreeNode> tree(Integer type) {
        List<TreeNode> treeNodes = new ArrayList<>();
        if (null == type) {
            treeNodes.addAll(groupService.treeNodes(0));
        } else {
            treeNodes.addAll(groupService.treeNodes(type));
        }

        return treeNodes;
    }

    @GetMapping("tree-all")
    @ApiOperation(value = "所有群组的树列表")
    public List<TreeNode> treeAll() {
        List<TreeNode> treeNodes = new ArrayList<>();
        TreeNode treeNode = new TreeNode();
        treeNode.setTitle("我的群组");
        treeNode.setValue("1");
        treeNode.setSelectable(false);
        treeNode.setChildren(groupService.treeNodes(1));
        treeNodes.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setTitle("公司群组");
        treeNode.setValue("0");
        treeNode.setSelectable(false);
        treeNode.setChildren(groupService.treeNodes(0));
        treeNodes.add(treeNode);

        return treeNodes;
    }

    /**
     * 用户
     *
     * @param gid         gid
     * @param pageRequest pageRequest
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see TableResponse
     */
    @GetMapping("user")
    @ApiOperation(value = "分组下的用户列表")
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
    @ApiOperation(value = "从分组下移除用户")
    public ApiResponse<Integer> user(Long gid, Long uid) {
        UserGroup userGroup = new UserGroup();
        userGroup.setUid(uid);
        userGroup.setGid(gid);
        return userGroupService.deleteByExample(userGroup);
    }

    @GetMapping("user/unlink")
    @ApiOperation(value = "未添加到当前分组的用户列表")
    public ApiResponse<TableResponse<User>> table(
            Long gid,
            User user,
            @Parameter(description = "页面要求", required = true) PageRequest pageRequest) {
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
    @ApiOperation(value = "添加用户到分组")
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

    @Override
    @ApiOperation(value = "删除分组")
    public ApiResponse<Integer> delete(@Parameter(description = "id", required = true, example = "") @PathVariable(name = "id") Long id,
                                       @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        return super.delete(id, request, response);
    }
}
