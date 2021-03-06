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
import io.github.xbeeant.eoffice.enums.ActionAuditEnum;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.rest.vo.*;
import io.github.xbeeant.eoffice.service.*;
import io.github.xbeeant.eoffice.service.render.RenderFactory;
import io.github.xbeeant.eoffice.util.AntDesignUtil;
import io.github.xbeeant.eoffice.util.LogHelper;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.mybatis.antdesign.PageRequest;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import io.github.xbeeant.spring.security.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/10/31
 */
@Api(tags = "????????????")
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
    @ApiOperation(value = "????????????????????????")
    public ApiResponse<Resource> rename(String name, Long rid, @ApiIgnore Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        Resource resource = new Resource();
        resource.setRid(rid);
        resource.setName(name);
        ApiResponse<Resource> resourceApiResponse = resourceService.updateByPrimaryKeySelective(resource);
        LogHelper.save(resourceApiResponse.getData(), ActionAuditEnum.RENAME, userSecurityUser);
        return resourceApiResponse;
    }

    /**
     * ????????????
     *
     * @param authentication ????????????
     * @param fid            ??????ID
     * @param pageRequest    pageRequest
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    @GetMapping
    @ApiOperation(value = "?????????????????????")
    public ApiResponse<List<Resource>> resources(@ApiIgnore Authentication authentication,
                                                 @RequestParam(defaultValue = "0", required = false) Long fid,
                                                 @ApiParam(name = "??????????????????") @RequestParam(value = "key", required = false) String keyWords,
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
     * ????????????
     *
     * @param rid ??????ID
     * @param vid ??????ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @GetMapping("info")
    @ApiOperation(value = "?????????????????????")
    public ApiResponse<ResourceInfo> info(Long rid, Long vid) {
        ApiResponse<ResourceInfo> result = new ApiResponse<>();
        ApiResponse<ResourceVo> resourceVo = resourceService.detail(rid, vid);
        if (!resourceVo.getSuccess()) {
            resourceVo.setResult(ErrorCodeConstant.NO_MATCH, "??????????????????");
            return result;
        }

        // ????????????????????????????????????????????????
        ResourceInfo resourceInfo = JsonHelper.copy(resourceVo.getData(), ResourceInfo.class);
        ApiResponse<User> userApiResponse = userService.selectByPrimaryKey(Long.valueOf(resourceInfo.getCreateBy()));
        if (userApiResponse.getSuccess()) {
            resourceInfo.setCreateBy(userApiResponse.getData().getNickname());
        }

        // ??????????????????????????????
        ApiResponse<PageResponse<ResourcePerm>> permed = resourceService.permed(rid, new PageBounds(0, 10000));
        resourceInfo.setPermed(permed.getData());
        result.setData(resourceInfo);
        return result;
    }

    /**
     * ????????????
     *
     * @param authentication ????????????
     * @param rid            ??????ID
     * @param vid            ??????ID
     * @param share          ????????????Key
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @GetMapping("detail")
    @ApiOperation(value = "?????????????????????")
    public ApiResponse<ResourceVo> detail(@ApiIgnore Authentication authentication,
                                          Long rid,
                                          Long vid,
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
            // ??????
            permission = resourceService.permission(rid, Long.valueOf(userSecurityUser.getUserId()));
        }

        if (Boolean.FALSE.equals(permission.hasPermission())) {
            ApiResponse<ResourceVo> response = new ApiResponse<>();
            response.setResult(100, "???????????????????????????????????????????????????");
            return response;
        }

        ApiResponse<ResourceVo> resourceInfo = resourceService.detail(rid, vid);
        if (!resourceInfo.getSuccess()) {
            resourceInfo.setResult(ErrorCodeConstant.NO_MATCH, "??????????????????");
            return resourceInfo;
        }
        resourceInfo.getData().setPerm(permission);
        if (vid != null) {
            ApiResponse<ResourceVersion> versionApiResponse = resourceVersionService.selectByPrimaryKey(vid);

            if (!resourceInfo.getData().getSid().equals(versionApiResponse.getData().getSid())) {
                resourceInfo.getData().getPerm().setEdit(false);
            }
        }

        RenderFactory.getRender(render).setExtra(resourceInfo.getData(), mode, userSecurityUser);
        if (StringUtils.isEmpty(share)) {
            LogHelper.save(resourceInfo.getData(), ActionAuditEnum.VIEW, userSecurityUser);
        } else {
            LogHelper.save(resourceInfo.getData(), ActionAuditEnum.VIEW_SHARE, userSecurityUser);
        }

        return resourceInfo;
    }

    /**
     * ????????????
     *
     * @param authentication ????????????
     * @param shareId        ??????ID
     * @param authCode       ?????????
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @PostMapping("share")
    @ApiOperation(value = "????????????")
    public ApiResponse<ShareResponse> share(@ApiIgnore Authentication authentication, Long shareId, String authCode) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
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

        LogHelper.save(resourceApiResponse.getData(), ActionAuditEnum.SHARE, userSecurityUser);
        return response;
    }

    /**
     * ??????
     *
     * @param authentication ????????????
     * @param rid            ??????ID
     * @param value          ???
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see String
     */
    @PostMapping("")
    @ApiOperation(value = "????????????")
    public ApiResponse<Resource> save(@ApiIgnore Authentication authentication, Long rid, String value) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ApiResponse<Resource> response = resourceService.save(rid, value, userSecurityUser.getUserId());
        LogHelper.save(response.getData(), ActionAuditEnum.UPDATE, userSecurityUser);
        return response;
    }

    /**
     * ????????????????????????????????????
     *
     * @param rids ??????id
     * @param fid  ??????ID
     * @return ???????????????????????????
     */
    @PostMapping("move")
    @ResourceOwner(id = "rids", selectService = IResourceService.class)
    @ApiOperation(value = "??????(???)??????")
    public ApiResponse<Integer> move(@RequestParam(value = "rid") List<Long> rids, Long fid,
                                     @RequestParam(defaultValue = "0", required = false) Long fromFid,
                                     @ApiIgnore Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ApiResponse<Integer> move = resourceService.move(rids, fid, fromFid);
        for (Long rid : rids) {
            LogHelper.save(rid, ActionAuditEnum.MOVE, userSecurityUser);
        }

        return move;
    }

    /**
     * ??????
     *
     * @param rids ??????ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Integer
     */
    @DeleteMapping("")
    @Transactional
    @ResourceOwner(id = "rids", selectService = IResourceService.class)
    @ApiOperation(value = "?????????????????????")
    public ApiResponse<Integer> delete(@RequestParam(value = "rid") List<Long> rids, @ApiIgnore Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        for (Long rid : rids) {
            // ??????????????????????????????????????????????????????????????????????????????????????????
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
                    response.setResult(ErrorCodeConstant.CONFLICT, resourceApiResponse.getData().getName() + "????????????????????????????????????????????????????????????????????????????????????");
                    return response;
                }
                // ???????????????
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

            // ????????????????????????
            folderService.updateFolderSize(data.getFid(), -data.getSize());

            LogHelper.save(resourceApiResponse.getData(), ActionAuditEnum.DELETE, userSecurityUser);
        }

        return new ApiResponse<>();
    }

    @DeleteMapping("perm")
    @ApiOperation(value = "???????????????????????????")
    public ApiResponse<Integer> removePerm(Long pid,@ApiIgnore Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ApiResponse<Perm> permApiResponse = permService.selectByPrimaryKey(pid);
        ApiResponse<Integer> response = permService.deleteByPrimaryKey(pid);
        LogHelper.save(permApiResponse.getData(), ActionAuditEnum.PERM_REMOVE, userSecurityUser);
        return response;
    }

    @PostMapping("perm")
    @ResourceOwner(id = "rid", selectService = IResourceService.class)
    @ApiOperation(value = "?????????????????????")
    public ApiResponse<String> perm(@RequestParam(value = "users", required = false) List<Long> users,
                                    @RequestParam(value = "team", required = false) List<Long> team,
                                    @RequestParam(value = "perm") List<String> perm,
                                    String type, Long rid,@ApiIgnore Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ApiResponse<String> response;
        if ("member".equals(type)) {
            response = resourceService.perm(users, perm, PermTargetType.MEMBER, rid);
            LogHelper.save(rid, users, ActionAuditEnum.PERM, userSecurityUser);
        } else {
            response = resourceService.perm(team, perm, PermTargetType.TEAM, rid);
            LogHelper.save(rid, team, ActionAuditEnum.PERM, userSecurityUser);
        }
        return response;
    }

    @GetMapping("perm")
    @ApiOperation(value = "???????????????????????????")
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
     * ????????????
     *
     * @param authentication ????????????
     * @param file           ??????
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload")
    @ApiOperation(value = "????????????")
    public ApiResponse<Storage> upload(@ApiIgnore Authentication authentication, MultipartFile file) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        return resourceService.upload(file, userSecurityUser.getUserId());
    }


    /**
     * ????????????
     *
     * @param fid       ??????ID
     * @param filesJson ??????
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload/save")
    @ApiOperation(value = "?????????????????????")
    public ApiResponse<List<ResourceVo>> uploadSave(@ApiIgnore Authentication authentication, Long fid, @RequestParam(value = "files") String filesJson) {
        List<Storage> files = JSON.parseArray(filesJson, Storage.class);
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ApiResponse<List<ResourceVo>> objectApiResponse = new ApiResponse<>();
        List<ResourceVo> resourceVos = new ArrayList<>();
        for (Storage storage : files) {
            ResourceVo resourceVo = resourceService.saveResource(fid, userSecurityUser.getUserId(), storage.getName(), storage);
            resourceVos.add(resourceVo);

            LogHelper.save(resourceVo, ActionAuditEnum.UPLOAD, userSecurityUser);
        }
        objectApiResponse.setData(resourceVos);
        return objectApiResponse;
    }

    /**
     * ????????????
     *
     * @param filesJson ??????
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("upload/overwrite")
    @ApiOperation(value = "???????????????????????????")
    @ResourceOwner(id = "rid", selectService = IResourceService.class)
    public ApiResponse<String> overwrite(@ApiIgnore Authentication authentication, Long rid, @RequestParam(value = "files") String filesJson) {
        List<Storage> files = JSON.parseArray(filesJson, Storage.class);
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        for (Storage storage : files) {
            ResourceVo resourceVo = resourceService.overwriteResource(rid, userSecurityUser.getUserId(), storage);
            LogHelper.save(resourceVo, ActionAuditEnum.OVERWRITE, userSecurityUser);
        }

        return new ApiResponse<>();
    }

    /**
     * ??????????????????
     *
     * @param authentication ????????????
     * @param file           ??????
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Resource
     */
    @PostMapping("attachment")
    @ApiOperation(value = "????????????")
    public AttachmentResponse attachment(@ApiIgnore Authentication authentication, Long rid, MultipartFile file) throws NoSuchAlgorithmException, IOException {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        Storage storage = storageService.insert(file, userSecurityUser.getUserId());

        ResourceAttachment resourceAttachment = new ResourceAttachment();
        resourceAttachment.setRid(rid);
        resourceAttachment.setSid(storage.valueOfKey());
        ApiResponse<ResourceAttachment> resourceAttachmentApiResponse = resourceAttachmentService.insertSelective(resourceAttachment);

        AttachmentResponse attachmentResponse = new AttachmentResponse();
        attachmentResponse.setSuccess(true);
        attachmentResponse.setMessage("???????????????");
        attachmentResponse.setUrl("/eoffice/api/resource/attachment?rid=" + rid + "&aid=" + resourceAttachmentApiResponse.getData().getAid());
        return attachmentResponse;
    }

    /**
     * ??????
     *
     * @param rid      ??????ID
     * @param sid      ??????ID
     * @param request  ??????
     * @param response ??????
     */
    @RequestMapping(value = "s", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "????????????")
    public void download(Long rid, Long sid,@ApiIgnore  HttpServletRequest request,@ApiIgnore  HttpServletResponse response) {
        // todo ????????????
        resourceService.download(rid, sid, request, response);
    }

    @GetMapping("folder")
    @ApiOperation(value = "???????????????????????????")
    public List<TreeNode> folder() {
        SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();
        return folderService.hasPermissionFolders(userSecurityUser.getUserId());
    }

    /**
     * ????????????
     *
     * @param rid      ??????ID
     * @param aid      ??????ID
     * @param request  ??????
     * @param response ??????
     */
    @GetMapping("attachment")
    @ApiOperation(value = "????????????")
    public void attachment(Long rid, Long aid, HttpServletRequest request, HttpServletResponse response) {
        resourceService.downloadAttachment(rid, aid, request, response);
    }

    /**
     * ????????????
     *
     * @param type ??????
     * @param fid  ??????ID
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see ResourceVo
     */
    @PostMapping("add")
    @ApiOperation(value = "????????????")
    public ApiResponse<ResourceVo> add(String type,
                                       String name,
                                       @RequestParam(required = false) Long cid,
                                       @RequestParam(defaultValue = "0", required = false) Long fid,@ApiIgnore Authentication authentication) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();
        ResourceVo add = resourceService.add(type, name, fid, cid, userSecurityUser.getUserId());
        ApiResponse<ResourceVo> response = new ApiResponse<>();
        response.setData(add);

        LogHelper.save(response.getData(), ActionAuditEnum.ADD, userSecurityUser);
        return response;
    }

    @GetMapping("history")
    @ApiOperation(value = "????????????")
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
