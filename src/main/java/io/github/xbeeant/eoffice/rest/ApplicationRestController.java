package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.antdesign.CurrentUser;
import io.github.xbeeant.antdesign.MenuItem;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "首页模块")
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
    @ApiOperation(value = "登录用户有权限的目录菜单")
    public List<MenuItem> menu() {
        SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();

        // 我的文档菜单
        MenuItem folder = new MenuItem();
        folder.setName("我的");
        folder.setPath("/");
        folder.setKey("0");

        return folderService.hasPermissionMenus(userSecurityUser.getUserId());
    }

    @GetMapping("currentUser")
    @ApiOperation(value = "登录用户信息")
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
