package io.github.xbeeant.eoffice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.core.JsonHelper;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.exception.FileSaveFailedException;
import io.github.xbeeant.eoffice.exception.InvalidResourceMoveException;
import io.github.xbeeant.eoffice.exception.ResourceMissingException;
import io.github.xbeeant.eoffice.mapper.ResourceMapper;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.po.FullPerm;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.eoffice.rest.vo.ResourcePerm;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.eoffice.service.*;
import io.github.xbeeant.eoffice.service.storage.StorageFactory;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * eoffice_resource
 */
@Service
public class ResourceServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Resource, Long> implements IResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private IResourceAttachmentService resourceAttachmentService;

    @Autowired
    private IGroupService groupService;
    @Autowired
    private IFolderService folderService;

    @Autowired
    private IPermService permService;

    @Autowired
    private IResourceVersionService resourceVersionService;

    @Override
    public IMybatisPageHelperDao<Resource, Long> getRepositoryDao() {
        return this.resourceMapper;
    }

    @Override
    public void setDefaults(Resource record) {
        if (record.getRid() == null) {
            record.setRid(IdWorker.getId());
        }
    }

    @Override
    public ApiResponse<ResourceVo> detail(Long rid, Long vid) {
        ApiResponse<ResourceVo> result = new ApiResponse<>();
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        if (!resourceApiResponse.getSuccess()) {
            throw new ResourceMissingException("????????????");
        }
        ResourceVo resourceVo = JsonHelper.copy(resourceApiResponse.getData(), ResourceVo.class);
        // storage??????
        ApiResponse<Storage> storageApiResponse;
        if (vid == null ) {
            storageApiResponse = storageService.selectByPrimaryKey(resourceVo.getSid());
        } else {
            ApiResponse<ResourceVersion> resourceVersionApiResponse = resourceVersionService.selectByPrimaryKey(vid);
            ResourceVersion resourceVersion = resourceVersionApiResponse.getData();
            storageApiResponse = storageService.selectByPrimaryKey(resourceVersion.getSid());
            resourceVo.setName(resourceVersion.getName());
            resourceVo.setExtension(resourceVersion.getExtension());
            resourceVo.setSid(resourceVersion.getSid());
        }

        resourceVo.setStorage(storageApiResponse.getData());

        // owner??????
        ApiResponse<User> userApiResponse = userService.selectByPrimaryKey(Long.valueOf(resourceVo.getCreateBy()));
        User owner = new User();
        User user = userApiResponse.getData();
        owner.setUsername(user.getUsername());
        owner.setNickname(user.getNickname());
        resourceVo.setOwner(owner);

        StringBuilder url = new StringBuilder();
        url.append("/eoffice/api/resource/s?sid=" + resourceVo.getSid() + "&rid=" + rid);
        if (vid != null) {
            url.append( "&vid=" + vid);
        }
        resourceVo.setUrl(url.toString());

        result.setData(resourceVo);
        return result;
    }

    @Override
    public void download(Long rid, Long sid, HttpServletRequest request, HttpServletResponse response) {
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);

        ApiResponse<Storage> storageApiResponse = storageService.selectByPrimaryKey(sid);
        if (storageApiResponse.getSuccess()) {
            StorageFactory.getStorage(storageApiResponse.getData().getExtension()).download(storageApiResponse.getData(), resourceApiResponse.getData(), response, request);
        }
    }

    @Override
    public void downloadAttachment(Long rid, Long aid, HttpServletRequest request, HttpServletResponse response) {
        ApiResponse<ResourceAttachment> attachmentApiResponse = resourceAttachmentService.selectByPrimaryKey(aid);
        if (attachmentApiResponse.getData().getRid().equals(rid)) {
            Long sid = attachmentApiResponse.getData().getSid();
            ApiResponse<Storage> storageApiResponse = storageService.selectByPrimaryKey(sid);
            // todo
            FileHelper.download(storageApiResponse.getData(), "", response, request);
        } else {
            Requests.writeJson(response, "???????????????");
        }
    }

    @Override
    public ApiResponse<List<Resource>> hasPermissionResources(String keyWords, String uid, PageBounds pageBounds) {
        ApiResponse<List<Resource>> result = new ApiResponse<>();

        List<Resource> resources = resourceMapper.hasPermissionResourcesByKeyWord("%" + keyWords + "%", uid);
        result.setData(resources);
        return result;
    }

    @Override
    public ApiResponse<List<Resource>> hasPermissionResources(Long fid, String uid, PageBounds pageBounds) {
        ApiResponse<List<Resource>> result = new ApiResponse<>();

        // ??????fid = 0 ????????????????????????????????????????????? ?????? ????????????????????????????????????
        List<Resource> resources;
        if (fid == 0) {
            if (!StringUtils.isEmpty(pageBounds.getOrders())) {
                PageMethod.orderBy(pageBounds.getOrders());
            } else {
                PageMethod.orderBy("create_at desc");
            }
            Long lUid = Long.valueOf(uid);
            // ??????ID??????????????????ID??????
            UserGroup userGroupExample = new UserGroup();
            userGroupExample.setUid(lUid);
            ApiResponse<List<UserGroup>> listApiResponse = userGroupService.selectAllByExample(userGroupExample);
            Set<Long> gids = new HashSet<>();
            if (listApiResponse.getSuccess()) {
                for (UserGroup item : listApiResponse.getData()) {
                    List<Long> ids = groupService.parentIds(item.getGid());
                    gids.addAll(ids);
                }
            }
            resources = resourceMapper.hasPermissionResources(fid, uid, gids);
        } else {
            Perm perm = permService.perm(Long.valueOf(uid), fid);
            if (Boolean.FALSE.equals(perm.hasPermission())) {
                result.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
                return result;
            }
            if (!StringUtils.isEmpty(pageBounds.getOrders())) {
                PageMethod.orderBy(pageBounds.getOrders());
            } else {
                PageMethod.orderBy("create_at desc");
            }
            resources = resourceMapper.folderResources(fid);
        }

        result.setData(resources);
        return result;
    }

    @Override
    public ApiResponse<Resource> save(Long rid, String value, String uid) {
        // ????????????
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        Resource resource = resourceApiResponse.getData();
        Storage storage;
        try {
            storage = storageService.insert(value, resource.getName(), uid);
        } catch (Exception e) {
            throw new FileSaveFailedException(e);
        }

        // ??????????????????
        resource = new Resource();
        resource.setRid(rid);
        resource.setName(storage.getName());
        resource.setExtension(storage.getExtension());
        resource.setSid(storage.getSid());
        resource.setSize(storage.getSize());
        resource.setCreateBy(uid);
        resource.setUpdateBy(uid);
        updateByPrimaryKeySelective(resource);

        // ??????????????????
        ResourceVersion resourceVersion = new ResourceVersion();
        resourceVersion.setRid(rid);
        resourceVersion.setSid(storage.getSid());
        resourceVersion.setSize(storage.getSize());
        resourceVersion.setName(storage.getName());
        resourceVersion.setExtension(storage.getExtension());
        resourceVersionService.insertSelective(resourceVersion);
        ApiResponse<Resource> result = new ApiResponse<>();

        result.setData(resource);
        result.setMsg("????????????");
        return result;
    }

    @Override
    public ApiResponse<Storage> upload(MultipartFile file, String uid) {
        Storage storage;
        try {
            storage = storageService.insert(file, uid);
        } catch (Exception e) {
            throw new FileSaveFailedException(e);
        }

        ApiResponse<Storage> result = new ApiResponse<>();
        result.setData(storage);
        return result;
    }

    @Override
    public ResourceVo overwriteResource(Long rid, String uid, Storage storage) {
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        Resource dbResource = resourceApiResponse.getData();

        // ??????????????????
        ResourceVo resource = new ResourceVo();
        resource.setRid(rid);
        resource.setName(storage.getName());
        resource.setExtension(storage.getExtension());
        resource.setSid(storage.getSid());
        resource.setSize(storage.getSize());
        resource.setCreateBy(uid);
        resource.setUpdateBy(uid);
        updateByPrimaryKeySelective(resource);

        // ??????????????????
        ResourceVersion resourceVersion = new ResourceVersion();
        resourceVersion.setRid(rid);
        resourceVersion.setSid(storage.getSid());
        resourceVersion.setSize(storage.getSize());
        resourceVersion.setName(storage.getName());
        resourceVersion.setExtension(storage.getExtension());
        resourceVersion.setCreateBy(uid);
        resourceVersionService.insertSelective(resourceVersion);

        // ??????????????????????????????
        folderService.updateFolderAndResourceSize(dbResource.getFid(), dbResource.getSize(), resource.getSize());

        return resource;
    }

    @Override
    public ResourceVo saveResource(Long fid, String uid, String filename, Storage storage) {
        //
        Folder folder = new Folder();
        if (fid == null || 0L == fid) {
            // ?????????????????????
            folder.setFid(0L);
            folder.setPath("/");
        } else {
            // ?????????????????????
            folder = folderService.selectByPrimaryKey(fid).getData();
        }

        if (!filename.contains(".")) {
            filename = filename + "." + storage.getExtension();
        }

        // ??????????????????
        ResourceVo resource = new ResourceVo();
        resource.setPath(folder.getPath());
        resource.setName(filename);
        resource.setExtension(storage.getExtension());
        resource.setSid(storage.getSid());
        resource.setSize(storage.getSize());
        resource.setCreateBy(uid);
        resource.setUpdateBy(uid);
        resource.setFid(fid);
        insertSelective(resource);

        // ??????????????????
        Perm perm = new FullPerm();
        perm.setCreateBy(uid);
        perm.setRid(resource.getRid());
        perm.setTargetId(Long.valueOf(uid));
        perm.setType(PermType.FILE.getType());
        perm.setTargetType(PermTargetType.MEMBER.getType());
        permService.insertSelective(perm);

        // ??????????????????????????????
        folderService.updateFolderSize(fid, resource.getSize());

        // ??????????????????
        ResourceVersion resourceVersion = new ResourceVersion();
        resourceVersion.setRid(resource.getRid());
        resourceVersion.setSid(storage.getSid());
        resourceVersion.setSize(storage.getSize());
        resourceVersion.setName(storage.getName());
        resourceVersion.setExtension(storage.getExtension());
        resourceVersionService.insertSelective(resourceVersion);

        resource.setPerm(perm);
        return resource;
    }

    @Override
    public Perm permission(Long rid, Long userId) {
        return permService.perm(userId, rid);
    }

    @Override
    public Perm sharePermission(Long rid, Long userId, Share share) {
        return permService.sharePerm(userId, rid, share);
    }

    @Override
    public ResourceVo add(String type, String name, Long fid, Long cid, String uid) {
        Storage storage = StorageFactory.getStorage(type).add(type, fid, cid, uid);
        return saveResource(fid, uid, name, storage);
    }

    @Override
    public List<User> users(Long rid) {
        return permService.users(rid);
    }

    @Override
    public ApiResponse<String> perm(List<Long> targetId, List<String> perm, PermTargetType targetType, Long rid) {
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        if (!resourceApiResponse.getSuccess()) {
            ApiResponse<String> result = new ApiResponse<>();
            result.setResult(ErrorCodeConstant.NO_MATCH, "??????????????????");
            return result;
        }
        PermType type = "folder".equals(resourceApiResponse.getData().getExtension()) ? PermType.FOLDER : PermType.FILE;
        return permService.perm(targetId, perm, resourceApiResponse.getData(), type, targetType);
    }

    @Override
    public ApiResponse<Integer> move(List<Long> rids, Long targetFid, Long fromFid) {
        ApiResponse<Integer> response = new ApiResponse<>();

        // ???????????????????????????????????????
        Folder sourceFolder = new Folder();
        if (0L != fromFid) {
            ApiResponse<Folder> sourceFolderResponse = folderService.selectByPrimaryKey(fromFid);
            sourceFolder = sourceFolderResponse.getData();
            if (Boolean.FALSE.equals(sourceFolderResponse.getSuccess())) {
                response.setResult(ErrorCodeConstant.NO_MATCH, "??????????????????????????????????????????????????????");
                return response;
            }
        }

        ApiResponse<Folder> targetFolderResponse = folderService.selectByPrimaryKey(targetFid);

        if (!targetFolderResponse.getSuccess() || targetFolderResponse.getData().getDeleted()) {
            response.setResult(100, "??????????????????????????????");
            return response;
        }

        Integer moveFailedCount = moveFolder(rids, targetFolderResponse.getData(), sourceFolder);
        if (moveFailedCount > 0) {
            response.setResult(ErrorCodeConstant.UPDATE_NONE, "????????????");
            return response;
        }
        response.setCode(0);
        return response;
    }

    private Integer moveFolder(List<Long> rids, Folder targetFolder, Folder sourceFolder) {
        // ???????????????????????????????????????????????????
        List<Folder> subFolders = folderService.subFolders(sourceFolder.getFid());

        for (Long rid : rids) {
            ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
            if (resourceApiResponse.getSuccess()) {
                Resource resource = resourceApiResponse.getData();

                // ?????????????????????
                Resource updateResource = new Resource();
                updateResource.setRid(rid);
                updateResource.setFid(targetFolder.getFid());
                updateResource.setPath(targetFolder.getPath());
                // todo path?????????????????????????????????????????????????????????????????????
                if ("folder".equals(resource.getExtension())) {
                    for (Folder folder : subFolders) {
                        if (folder.getFid().equals(targetFolder.getFid())) {
                            throw new InvalidResourceMoveException("??????????????????????????????");
                        }
                    }

                    // ??????folder??????
                    Folder updateFolder = new Folder();
                    updateFolder.setFid(rid);
                    updateFolder.setPfid(targetFolder.getFid());
                    updateFolder.setPath(targetFolder.getPath() + "/" + resource.getName());

                    folderService.updateFolderSize(targetFolder.getFid(), resource.getSize());
                    folderService.updateFolderSize(sourceFolder.getFid(), -resource.getSize());

                    folderService.updateByPrimaryKeySelective(updateFolder);
                    updateResource.setPath(updateFolder.getPath());
                }
                updateByPrimaryKeySelective(updateResource);
            }
        }

        return 0;
    }

    @Override
    public ApiResponse<PageResponse<ResourcePerm>> permed(Long rid, PageBounds pageBounds) {

        ApiResponse<PageResponse<ResourcePerm>> apiResponse = new ApiResponse<>();

        PageMethod.orderBy(pageBounds.getOrders());
        PageMethod.startPage(pageBounds.getPage(), pageBounds.getLimit());
        Page<ResourcePerm> result = resourceMapper.permed(rid);
        if (result.isEmpty()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }
        PageResponse<ResourcePerm> pageList = new PageResponse<>(result, result.getTotal(), pageBounds.getPage());
        apiResponse.setData(pageList);

        return apiResponse;
    }
}
