package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.util.AntDesignUtil;
import io.github.xbeeant.spring.mybatis.antdesign.PageRequest;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/10/31
 */
@RestController
@RequestMapping("api/resource")
public class ResourceRestController {

    @Autowired
    private IResourceService resourceService;

    @GetMapping
    public ApiResponse<List<Resource>> resources(Authentication authentication,
                                                 @RequestParam(defaultValue = "0", required = false) Long fid,
                                                 PageRequest pageRequest) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        ApiResponse<List<Resource>> apiResponse;

        String sorter = AntDesignUtil.translateOrder(pageRequest.getSorter());
        pageRequest.setSorter(sorter);

        // todo 权益
        apiResponse = resourceService.hasPermissionResources(fid, userSecurityUser.getUserId(), pageRequest.getPageBounds());
        apiResponse.setCode(0);

        return apiResponse;
    }

    @PostMapping("upload")
    public ApiResponse<Resource> upload(Authentication authentication,
                                        Long fid,
                                        MultipartFile file) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        return resourceService.insert(file, fid, userSecurityUser.getUserId());
    }
}
