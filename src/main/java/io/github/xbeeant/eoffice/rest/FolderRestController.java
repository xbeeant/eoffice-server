package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.enums.ActionAuditEnum;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.rest.vo.Breadcrumb;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.eoffice.util.LogHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/11/1
 */
@Api(tags = "文件夹模块")
@RestController
@RequestMapping("api/folder")
public class FolderRestController {

    @Autowired
    private IFolderService folderService;

    /**
     * 创建文件夹
     *
     * @param name 文件夹名称
     * @param pfid 父文件夹ID
     * @param authentication 认证信息
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Folder
     */
    @PostMapping()
    @ApiOperation(value = "创建文件夹")
    public ApiResponse<Folder> post(@RequestParam String name,
                                    @RequestParam(defaultValue = "0", required = false) Long pfid,
                                    Authentication authentication) {
        SecurityUser<User> principal = (SecurityUser<User>) authentication.getPrincipal();
        Folder folder = new Folder();
        folder.setName(name);
        folder.setPfid(pfid);
        folder.setCreateBy(principal.getUserId());

        ApiResponse<Folder> response = folderService.insertSelective(folder);
        LogHelper.save(response.getData(), ActionAuditEnum.CREATE_FOLDER, principal);
        return response;
    }

    /**
     * 父文件夹信息
     *
     * @param fid 文件夹ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    @GetMapping("breadcrumb")
    @ApiOperation(value = "文件夹面包屑导航")
    public ApiResponse<List<Breadcrumb>> breadcrumb(Long fid) {
        ApiResponse<List<Breadcrumb>> result = new ApiResponse<>();
        List<Folder> parents = folderService.breadcrumb(fid);
        Collections.reverse(parents);
        List<Breadcrumb> breadcrumbs = new ArrayList<>(parents.size());

        for (Folder parent : parents) {
            Breadcrumb breadcrumb = new Breadcrumb(parent.getName(), parent.getFid());
            breadcrumbs.add(breadcrumb);
        }

        result.setData(breadcrumbs);
        return result;
    }
}
