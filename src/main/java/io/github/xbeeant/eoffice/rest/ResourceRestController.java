package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.ResourceAttachment;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.rest.vo.AttachmentResponse;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.eoffice.service.IResourceAttachmentService;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.service.IStorageService;
import io.github.xbeeant.eoffice.util.AntDesignUtil;
import io.github.xbeeant.spring.mybatis.antdesign.PageRequest;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IResourceAttachmentService resourceAttachmentService;

    /**
     * 资源列表
     *
     * @param authentication 身份验证
     * @param fid            支撑材
     * @param pageRequest    pageRequest
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
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

    @GetMapping("detail")
    public ApiResponse<ResourceVo> resources(Authentication authentication, Long rid) {
        ApiResponse<ResourceVo> resourceInfo = resourceService.detail(rid);

        // 权限
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        return resourceInfo;
    }

    @PostMapping("")
    public ApiResponse<String> save(Authentication authentication, Long rid, String value) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        return resourceService.save(rid, value, userSecurityUser.getUserId());
    }

    /**
     * 资源上传
     *
     * @param authentication 身份验证
     * @param fid            支撑材
     * @param file           文件
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload")
    public ApiResponse<Resource> upload(Authentication authentication,
                                        Long fid,
                                        MultipartFile file) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        return resourceService.insert(file, fid, userSecurityUser.getUserId());
    }

    /**
     * 资源附件上传
     *
     * @param authentication 身份验证
     * @param file           文件
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("attachment")
    public AttachmentResponse attachment(Authentication authentication,
                                         Long rid,
                                         MultipartFile file) throws NoSuchAlgorithmException, IOException {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        Storage storage = storageService.insert(file, userSecurityUser.getUserId());

        ResourceAttachment resourceAttachment = new ResourceAttachment();
        resourceAttachment.setRid(rid);
        resourceAttachment.setSid(storage.valueOfKey());
        ApiResponse<ResourceAttachment> resourceAttachmentApiResponse = resourceAttachmentService.insertSelective(resourceAttachment);

        AttachmentResponse attachmentResponse = new AttachmentResponse();
        attachmentResponse.setSuccess(true);
        attachmentResponse.setMessage("上传成功！");
        attachmentResponse.setUrl("./api/resource/attachment?rid=" + rid + "&aid=" + resourceAttachmentApiResponse.getData().getAid());
        return attachmentResponse;
    }

    @RequestMapping(value = "s", method = {RequestMethod.GET, RequestMethod.POST})
    public void download(Long rid, Long sid, HttpServletRequest request, HttpServletResponse response) {
        resourceService.download(rid, sid, request, response);
    }

    @GetMapping("attachment")
    public void attachment(Long rid, Long aid, HttpServletRequest request, HttpServletResponse response) {
        resourceService.downloadAttachment(rid, aid, request, response);
    }
}
