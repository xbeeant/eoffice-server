package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaobiao
 * @version 2021/11/1
 */
@RestController
@RequestMapping("api/folder")
public class FolderRestController {

    @Autowired
    private IFolderService folderService;

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
}
