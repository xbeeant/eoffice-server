package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.antdesign.TableRequest;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.eoffice.model.*;
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
     * @param fid            文件ID
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

    /**
     * 资源详情
     *
     * @param authentication 身份验证
     * @param rid            资源ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @GetMapping("detail")
    public ApiResponse<ResourceVo> detail(Authentication authentication, Long rid) {
        ApiResponse<ResourceVo> resourceInfo = resourceService.detail(rid);
        if (!resourceInfo.getSuccess()) {
            resourceInfo.setResult(ErrorCodeConstant.NO_MATCH, "文件已经丢失");
            return resourceInfo;
        }
        // 权限
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        Perm permission = resourceService.permission(rid, Long.valueOf(userSecurityUser.getUserId()));
        resourceInfo.getData().setPerm(permission);
        return resourceInfo;
    }

    /**
     * 保存
     *
     * @param authentication 身份验证
     * @param rid            资源ID
     * @param value          值
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    @PostMapping("")
    public ApiResponse<String> save(Authentication authentication, Long rid, String value) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        return resourceService.save(rid, value, userSecurityUser.getUserId());
    }

    /**
     * 资源上传
     *
     * @param authentication 身份验证
     * @param fid            文件ID
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

        return resourceService.upload(file, fid, userSecurityUser.getUserId());
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

    /**
     * 下载
     *
     * @param rid      资源ID
     * @param sid      存储ID
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping(value = "s", method = {RequestMethod.GET, RequestMethod.POST})
    public void download(Long rid, Long sid, HttpServletRequest request, HttpServletResponse response) {
        resourceService.download(rid, sid, request, response);
    }

    /**
     * 附件下载
     *
     * @param rid      资源ID
     * @param aid      附件ID
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("attachment")
    public void attachment(Long rid, Long aid, HttpServletRequest request, HttpServletResponse response) {
        resourceService.downloadAttachment(rid, aid, request, response);
    }

    /**
     * 资源新建
     *
     * @param type 类型
     * @param fid  目录ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @PostMapping("add")
    public ApiResponse<ResourceVo> add(String type,
                                       @RequestParam(defaultValue = "0", required = false) Long fid,
                                       Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        resourceService.add(type, fid, userSecurityUser.getUserId());
        return new ApiResponse<>();
    }
}
