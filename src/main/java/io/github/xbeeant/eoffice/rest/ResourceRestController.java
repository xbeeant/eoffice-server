package io.github.xbeeant.eoffice.rest;

import com.alibaba.fastjson.JSON;
import io.github.xbeeant.antdesign.TableResponse;
import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.JsonHelper;
import io.github.xbeeant.crypto.asymmetric.RSAUtil;
import io.github.xbeeant.eoffice.aspect.annotation.ResourceOwner;
import io.github.xbeeant.eoffice.config.RsaKeyProperties;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.rest.vo.*;
import io.github.xbeeant.eoffice.service.*;
import io.github.xbeeant.eoffice.service.render.RenderFactory;
import io.github.xbeeant.eoffice.util.AntDesignUtil;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.mybatis.antdesign.PageRequest;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import io.github.xbeeant.spring.security.SecurityUser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/10/31
 */
@RestController
@RequestMapping("api/resource")
public class ResourceRestController {

    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    @Autowired
    private IUserService userService;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IShareService shareService;

    @Autowired
    private IPermService permService;

    @Autowired
    private IResourceAttachmentService resourceAttachmentService;

    @Autowired
    private IFolderService folderService;


    @Autowired
    private IResourceVersionService resourceVersionService;

    @PostMapping("rename")
    public ApiResponse<Resource> rename(String name, Long rid) {
        Resource resource = new Resource();
        resource.setRid(rid);
        resource.setName(name);
        return resourceService.updateByPrimaryKeySelective(resource);
    }

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
                                                 @RequestParam(value = "key", required = false) String keyWords,
                                                 PageRequest pageRequest) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        ApiResponse<List<Resource>> apiResponse;

        String sorter = AntDesignUtil.translateOrder(pageRequest.getSorter());
        pageRequest.setSorter(sorter);
        if (!StringUtils.isEmpty(keyWords)) {
            apiResponse = resourceService.hasPermissionResources(keyWords, userSecurityUser.getUserId(), pageRequest.getPageBounds());
        } else {
            apiResponse = resourceService.hasPermissionResources(fid, userSecurityUser.getUserId(), pageRequest.getPageBounds());
        }

        apiResponse.setCode(0);

        return apiResponse;
    }

    /**
     * 资源信息
     *
     * @param rid 资源ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @GetMapping("info")
    public ApiResponse<ResourceInfo> info(Long rid) {
        ApiResponse<ResourceInfo> result = new ApiResponse<>();
        ApiResponse<ResourceVo> resourceVo = resourceService.detail(rid);
        if (!resourceVo.getSuccess()) {
            resourceVo.setResult(ErrorCodeConstant.NO_MATCH, "文件已经丢失");
            return result;
        }

        // 获取创建人信息，替换为创建人姓名
        ResourceInfo resourceInfo = JsonHelper.copy(resourceVo.getData(), ResourceInfo.class);
        ApiResponse<User> userApiResponse = userService.selectByPrimaryKey(Long.valueOf(resourceInfo.getCreateBy()));
        if (userApiResponse.getSuccess()) {
            resourceInfo.setCreateBy(userApiResponse.getData().getNickname());
        }

        // 当前资源已授权的用户
        ApiResponse<PageResponse<ResourcePerm>> permed = resourceService.permed(rid, new PageBounds(0, 10000));
        resourceInfo.setPermed(permed.getData());
        result.setData(resourceInfo);
        return result;
    }

    /**
     * 资源详情
     *
     * @param authentication 身份验证
     * @param rid            资源ID
     * @param share          分享访问Key
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @GetMapping("detail")
    public ApiResponse<ResourceVo> detail(Authentication authentication,
                                          Long rid,
                                          String share,
                                          String render,
                                          String mode,
                                          Long shareId) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, IOException, InvalidKeyException {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        Perm permission;
        if (!StringUtils.isEmpty(share)) {
            byte[] decrypt = RSAUtil.decrypt(rsaKeyProperties.getPrivateKey(), share);
            rid = Long.valueOf(new String(decrypt));
            ApiResponse<Share> shareApiResponse = shareService.selectByPrimaryKey(shareId);
            permission = resourceService.sharePermission(rid, Long.valueOf(userSecurityUser.getUserId()), shareApiResponse.getData());
        } else {
            // 权限
            permission = resourceService.permission(rid, Long.valueOf(userSecurityUser.getUserId()));
        }

        if (Boolean.FALSE.equals(permission.hasPermission())) {
            ApiResponse<ResourceVo> response = new ApiResponse<>();
            response.setResult(100, "尚未取得该文件授权，请联系作者获取");
            return response;
        }

        ApiResponse<ResourceVo> resourceInfo = resourceService.detail(rid);
        if (!resourceInfo.getSuccess()) {
            resourceInfo.setResult(ErrorCodeConstant.NO_MATCH, "文件已经丢失");
            return resourceInfo;
        }
        resourceInfo.getData().setPerm(permission);
        RenderFactory.getRender(render).setExtra(resourceInfo.getData(), mode, userSecurityUser);
        return resourceInfo;
    }

    /**
     * 资源详情
     *
     * @param authentication 身份验证
     * @param shareId        分享ID
     * @param authCode       提取码
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @PostMapping("share")
    public ApiResponse<ShareResponse> share(Authentication authentication, Long shareId, String authCode) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        ApiResponse<ShareResponse> response = new ApiResponse<>();
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ApiResponse<Resource> resourceApiResponse = shareService.avaliable(shareId, authCode, userSecurityUser.getUserId());

        if (!resourceApiResponse.getSuccess()) {
            response.setResult(resourceApiResponse.getCode(), resourceApiResponse.getMsg());
            return response;
        }

        Resource resource = resourceApiResponse.getData();

        byte[] encrypt = RSAUtil.encrypt(rsaKeyProperties.getPublicKey(), String.valueOf(resource.getRid()));

        ShareResponse shareResponse = new ShareResponse();
        shareResponse.setShare(new String(Base64.encodeBase64(encrypt)));
        shareResponse.setExtension(resource.getExtension());
        shareResponse.setShareId(shareId);
        response.setData(shareResponse);
        return response;
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
     * 文件移动（支持批量移动）
     *
     * @param rids 资源id
     * @param fid  目录ID
     * @return 返回移动的文件列表
     */
    @PostMapping("move")
    @ResourceOwner(id = "rids", selectService = IResourceService.class)
    public ApiResponse<Integer> move(@RequestParam(value = "rid") List<Long> rids, Long fid, @RequestParam(defaultValue = "0", required = false) Long fromFid) {
        return resourceService.move(rids, fid, fromFid);
    }

    /**
     * 删除
     *
     * @param rids 资源ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Integer
     */
    @DeleteMapping("")
    @Transactional
    @ResourceOwner(id = "rids", selectService = IResourceService.class)
    public ApiResponse<Integer> delete(@RequestParam(value = "rid") List<Long> rids) {
        for (Long rid : rids) {
            // 判断资源的类型，如果是文件夹，先要求清空文件夹后再删除文件夹
            ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(rid);
            if (!resourceApiResponse.getSuccess()) {
                return new ApiResponse<>();
            }

            Resource data = resourceApiResponse.getData();
            if ("folder".equals(data.getExtension())) {
                Resource example = new Resource();
                example.setDeleted(false);
                example.setFid(rid);
                ApiResponse<Resource> childItemResponse = resourceService.selectOneByExample(example);
                if (childItemResponse.getSuccess()) {
                    ApiResponse<Integer> response = new ApiResponse<>();
                    response.setResult(ErrorCodeConstant.CONFLICT, resourceApiResponse.getData().getName() + "文件夹下还有文件（夹），请先删除该文件夹下的文件（夹）！");
                    return response;
                }
                // 删除文件夹
                Folder folder = new Folder();
                folder.setFid(rid);
                folder.setDeleted(true);
                folder.setDeleteAt(new Date());
                folderService.updateByPrimaryKeySelective(folder);
            }

            Resource resource = new Resource();
            resource.setRid(rid);
            resource.setDeleted(true);
            resource.setDeleteAt(new Date());
            resourceService.updateByPrimaryKeySelective(resource);

            // 文件夹的空间减少
            folderService.updateFolderSize(data.getFid(), -data.getSize());
        }

        return new ApiResponse<>();
    }

    @DeleteMapping("perm")
    public ApiResponse<Integer> removePerm(Long pid) {
        return permService.deleteByPrimaryKey(pid);
    }

    @PostMapping("perm")
    @ResourceOwner(id = "rid", selectService = IResourceService.class)
    public ApiResponse<String> perm(@RequestParam(value = "users", required = false) List<Long> users,
                                    @RequestParam(value = "team", required = false) List<Long> team,
                                    @RequestParam(value = "perm") List<String> perm,
                                    String type, Long rid) {
        if ("member".equals(type)) {
            return resourceService.perm(users, perm, PermTargetType.MEMBER, rid);
        }

        return resourceService.perm(team, perm, PermTargetType.TEAM, rid);
    }

    @GetMapping("perm")
    public ApiResponse<TableResponse<ResourcePerm>> perm(Long rid, PageRequest pageRequest) {
        ApiResponse<TableResponse<ResourcePerm>> apiResponse = new ApiResponse<>();
        ApiResponse<PageResponse<ResourcePerm>> list = resourceService.permed(rid, pageRequest.getPageBounds());

        if (!list.getSuccess()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }

        TableResponse<ResourcePerm> pageResponse = new TableResponse<>();
        pageResponse.setList(list.getData());
        pageResponse.setPagination(list.getData().getPagination());
        apiResponse.setData(pageResponse);

        return apiResponse;
    }

    /**
     * 资源上传
     *
     * @param authentication 身份验证
     * @param file           文件
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload")
    public ApiResponse<Storage> upload(Authentication authentication, MultipartFile file) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        return resourceService.upload(file, userSecurityUser.getUserId());
    }


    /**
     * 资源上传
     *
     * @param fid       文件ID
     * @param filesJson 文件
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload/save")
    public ApiResponse<String> uploadSave(Authentication authentication, Long fid, @RequestParam(value = "files") String filesJson) {
        List<Storage> files = JSON.parseArray(filesJson, Storage.class);
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        for (Storage storage : files) {
            resourceService.saveResource(fid, userSecurityUser.getUserId(), storage.getName(), storage);
        }

        return new ApiResponse<>();
    }

    /**
     * 资源上传
     *
     * @param filesJson 文件
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload/overwrite")
    @ResourceOwner(id = "rid", selectService = IResourceService.class)
    public ApiResponse<String> overwrite(Authentication authentication, Long rid, @RequestParam(value = "files") String filesJson) {
        List<Storage> files = JSON.parseArray(filesJson, Storage.class);
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        for (Storage storage : files) {
            resourceService.overwriteResource(rid, userSecurityUser.getUserId(), storage);
        }

        return new ApiResponse<>();
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
    public AttachmentResponse attachment(Authentication authentication, Long rid, MultipartFile file) throws NoSuchAlgorithmException, IOException {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        Storage storage = storageService.insert(file, userSecurityUser.getUserId());

        ResourceAttachment resourceAttachment = new ResourceAttachment();
        resourceAttachment.setRid(rid);
        resourceAttachment.setSid(storage.valueOfKey());
        ApiResponse<ResourceAttachment> resourceAttachmentApiResponse = resourceAttachmentService.insertSelective(resourceAttachment);

        AttachmentResponse attachmentResponse = new AttachmentResponse();
        attachmentResponse.setSuccess(true);
        attachmentResponse.setMessage("上传成功！");
        attachmentResponse.setUrl("/eoffice/api/resource/attachment?rid=" + rid + "&aid=" + resourceAttachmentApiResponse.getData().getAid());
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

    @GetMapping("folder")
    public List<TreeNode> folder() {
        SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();
        return folderService.hasPermissionFolders(userSecurityUser.getUserId());
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
                                       String name,
                                       @RequestParam(required = false) Long cid,
                                       @RequestParam(defaultValue = "0", required = false) Long fid, Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        resourceService.add(type, name, fid, cid, userSecurityUser.getUserId());
        return new ApiResponse<>();
    }

    @GetMapping("history")
    public ApiResponse<TableResponse<ResourceVersionVo>> history(Long rid, PageRequest pageRequest) {
        ApiResponse<TableResponse<ResourceVersionVo>> apiResponse = new ApiResponse<>();
        PageRequest webRequest = new PageRequest(pageRequest);

        ResourceVersion example = new ResourceVersion();
        example.setRid(rid);

        ApiResponse<PageResponse<ResourceVersionVo>> list = resourceVersionService.fuzzySearchVoByPager(example, webRequest.getPageBounds());
        if (!list.getSuccess()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }

        TableResponse<ResourceVersionVo> pageResponse = new TableResponse<>();
        pageResponse.setList(list.getData());
        pageResponse.setPagination(list.getData().getPagination());
        apiResponse.setData(pageResponse);
        return apiResponse;
    }
}
