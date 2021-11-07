package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.antdesign.MenuItem;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/11/1
 */
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
    public ApiResponse<Folder> post(@RequestParam String name,
                                    @RequestParam(defaultValue = "0", required = false) Long pfid,
                                    Authentication authentication) {
        SecurityUser<User> principal = (SecurityUser<User>) authentication.getPrincipal();
        Folder folder = new Folder();
        folder.setName(name);
        folder.setPfid(pfid);
        folder.setCreateBy(principal.getUserId());

        return folderService.insertSelective(folder);
    }

    /**
     * 父文件夹信息
     *
     * @param fid 文件夹ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    @GetMapping("parents")
    public ApiResponse<MenuItem> parents(Long fid) {
        ApiResponse<MenuItem> result = new ApiResponse<>();
        MenuItem parents = folderService.parents(fid);
        result.setData(parents);
        return result;
    }
}
