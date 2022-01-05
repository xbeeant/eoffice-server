package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.antdesign.CurrentUser;
import io.github.xbeeant.antdesign.MenuItem;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/8/30
 */
@RestController
@RequestMapping("api")
public class ApplicationRestController {

    @Autowired
    private IFolderService folderService;


    /**
     * 文件夹菜单
     *
     * @return {@link List}
     * @see List
     * @see MenuItem
     */
    @GetMapping("menu")
    public List<MenuItem> menu() {
        SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();

        // 我的文档菜单
        MenuItem folder = new MenuItem();
        folder.setName("我的");
        folder.setPath("/");
        folder.setKey("0");

        return folderService.hasPermissionFolders(userSecurityUser.getUserId());
    }

    @GetMapping("currentUser")
    public ApiResponse<CurrentUser> current(Authentication authentication, HttpSession session) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        ApiResponse<CurrentUser> response = new ApiResponse<>();
        if (null == userSecurityUser) {
            response.setResult(401, "请先登录！");
            return response;
        }

        CurrentUser currentUser = new CurrentUser();
        currentUser.setName(userSecurityUser.getUserNickname());
        currentUser.setUserid(session.getId());
        response.setData(currentUser);
        return response;
    }
}
