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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * eoffice_resource
 */
@Service
public class ResourceServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Resource, Long> implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IResourceAttachmentService resourceAttachmentService;

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
    public ApiResponse<ResourceVo> detail(Long rid) {
        ApiResponse<ResourceVo> result = new ApiResponse<>();
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        if (!resourceApiResponse.getSuccess()) {
            throw new ResourceMissingException("文件丢了");
        }
        ResourceVo resourceVo = JsonHelper.copy(resourceApiResponse.getData(), ResourceVo.class);
        resourceVo.setUrl("/api/resource/s?sid=" + resourceApiResponse.getData().getSid() + "&rid=" + rid);
        result.setData(resourceVo);
        return result;
    }

    @Override
    public void download(Long rid, Long sid, HttpServletRequest request, HttpServletResponse response) {
        // todo 权益校验
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
            FileHelper.download(storageApiResponse.getData(), "", response, request);
        } else {
            Requests.writeJson(response, "资源不存在");
        }
    }

    @Override
    public ApiResponse<List<Resource>> hasPermissionResources(Long fid, String uid, PageBounds pageBounds) {
        ApiResponse<List<Resource>> result = new ApiResponse<>();


        // 如果fid = 0 ，在首页，获取已经授权的文件夹 否则 判断是否已经授权了文件夹
        List<Resource> resources;
        if (fid == 0) {
            if (!StringUtils.isEmpty(pageBounds.getOrders())) {
                PageMethod.orderBy(pageBounds.getOrders());
            } else {
                PageMethod.orderBy("create_at desc");
            }
            resources = resourceMapper.hasPermissionResources(fid, uid);
        } else {
            Perm perm = permService.perm(fid, Long.valueOf(uid), PermType.FOLDER);
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
    public ApiResponse<String> save(Long rid, String value, String uid) {
        // 资源信息
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        Resource resource = resourceApiResponse.getData();
        Storage storage;
        try {
            storage = storageService.insert(value, resource.getName(), uid);
        } catch (Exception e) {
            throw new FileSaveFailedException();
        }

        // 更新资源信息
        resource = new Resource();
        resource.setRid(rid);
        resource.setName(storage.getName());
        resource.setExtension(storage.getExtension());
        resource.setSid(storage.getSid());
        resource.setSize(storage.getSize());
        resource.setCreateBy(uid);
        resource.setUpdateBy(uid);
        updateByPrimaryKeySelective(resource);

        // 写入新的版本
        ResourceVersion resourceVersion = new ResourceVersion();
        resourceVersion.setRid(rid);
        resourceVersion.setSid(storage.getSid());
        resourceVersion.setSize(storage.getSize());
        resourceVersion.setName(storage.getName());
        resourceVersionService.insertSelective(resourceVersion);
        ApiResponse<String> result = new ApiResponse<>();
        result.setMsg("保存成功");
        return result;
    }

    @Override
    public ApiResponse<Storage> upload(MultipartFile file, Long fid, String uid) {
        Storage storage;
        try {
            storage = storageService.insert(file, uid);
        } catch (Exception e) {
            throw new FileSaveFailedException();
        }

        ApiResponse<Storage> result = new ApiResponse<>();
        result.setData(storage);
        return result;
    }

    @Override
    public ResourceVo overwriteResource(Long rid, String uid, Storage storage) {
        ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
        Resource dbResource = resourceApiResponse.getData();

        // 写入资源信息
        ResourceVo resource = new ResourceVo();
        resource.setRid(rid);
        resource.setName(storage.getName());
        resource.setExtension(storage.getExtension());
        resource.setSid(storage.getSid());
        resource.setSize(storage.getSize());
        resource.setCreateBy(uid);
        resource.setUpdateBy(uid);
        updateByPrimaryKeySelective(resource);

        // todo 版本信息记录
        // 更新文件夹的存储空间
        folderService.updateSize(dbResource.getFid(), dbResource.getSize(), resource.getSize());

        return resource;
    }

    @Override
    public ResourceVo saveResource(Long fid, String uid, Storage storage) {
        //
        Folder folder = new Folder();
        if (fid == null || 0L == fid) {
            // 为空，我的地盘
            folder.setFid(0L);
            folder.setPath("/");
        } else {
            // 获取文件夹信息
            folder = folderService.selectByPrimaryKey(fid).getData();
        }

        // 写入资源信息
        ResourceVo resource = new ResourceVo();
        resource.setPath(folder.getPath());
        resource.setName(storage.getName());
        resource.setExtension(storage.getExtension());
        resource.setSid(storage.getSid());
        resource.setSize(storage.getSize());
        resource.setCreateBy(uid);
        resource.setUpdateBy(uid);
        resource.setFid(fid);
        insertSelective(resource);

        // 写入权益信息
        Perm perm = new FullPerm();
        perm.setCreateBy(uid);
        perm.setRid(resource.getRid());
        perm.setTargetId(Long.valueOf(uid));
        perm.setType(PermType.FILE.getType());
        perm.setTargetType(PermTargetType.MEMBER.getType());
        permService.insertSelective(perm);

        // 更新文件夹的存储空间
        folderService.updateSize(fid, resource.getSize());

        resource.setPerm(perm);
        return resource;
    }

    @Override
    public Perm permission(Long rid, Long userId) {
        return permService.perm(userId, rid, PermType.FILE);
    }

    @Override
    public ResourceVo add(String type, Long fid, String uid) {
        Storage storage = StorageFactory.getStorage(type).add(type, fid, uid);
        return saveResource(fid, uid, storage);
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
            result.setResult(ErrorCodeConstant.NO_MATCH, "资源已被删除");
            return result;
        }
        PermType type = "folder".equals(resourceApiResponse.getData().getExtension()) ? PermType.FOLDER : PermType.FILE;
        return permService.perm(targetId, perm, rid, type, targetType);
    }

    @Override
    public ApiResponse<Integer> move(List<Long> rids, Long targetFid, Long fromFid) {
        ApiResponse<Integer> response = new ApiResponse<>();

        // 判断是否为源文件夹的创建人
        Folder sourceFolder = new Folder();
        if (0L != fromFid) {
            ApiResponse<Folder> sourceFolderResponse = folderService.selectByPrimaryKey(fromFid);
            sourceFolder = sourceFolderResponse.getData();
            if (Boolean.FALSE.equals(sourceFolderResponse.getSuccess())) {
                response.setResult(ErrorCodeConstant.NO_MATCH, "操作失败，找不到当前所属文件夹信息。");
                return response;
            }
        }
        ApiResponse<Folder> targetFolderResponse = folderService.selectByPrimaryKey(targetFid);

        if (!targetFolderResponse.getSuccess() || targetFolderResponse.getData().getDeleted()) {
            response.setResult(100, "目标文件夹已经被删除");
            return response;
        }

        Integer moveFailedCount = moveFolder(rids, targetFolderResponse.getData(), sourceFolder);
        if (moveFailedCount > 0) {
            response.setResult(ErrorCodeConstant.UPDATE_NONE, "移动失败");
            return response;
        }
        response.setCode(0);
        return response;
    }

    private Integer moveFolder(List<Long> rids, Folder targetFolder, Folder sourceFolder) {
        // 目标文件夹不能是源文件夹的子文件夹
        List<Folder> subFolders = folderService.subFolders(sourceFolder.getFid());

        for (Long rid : rids) {
            ApiResponse<Resource> resourceApiResponse = selectByPrimaryKey(rid);
            if (resourceApiResponse.getSuccess()) {
                Resource resource = resourceApiResponse.getData();

                // 移动文件（夹）
                Resource updateResource = new Resource();
                updateResource.setRid(rid);
                updateResource.setFid(targetFolder.getFid());
                updateResource.setPath(targetFolder.getPath());
                // todo path赋值存在问题，当文件夹移动到其他文件夹下的时候
                if ("folder".equals(resource.getExtension())) {
                    for (Folder folder : subFolders) {
                        if (folder.getFid().equals(targetFolder.getFid())) {
                            throw new InvalidResourceMoveException("不能移动到子文件夹中");
                        }
                    }
                    // 更新folder信息
                    Folder updateFolder = new Folder();
                    updateFolder.setFid(rid);
                    updateFolder.setPfid(targetFolder.getFid());
                    updateFolder.setPath(targetFolder.getPath() + "/" + resource.getName());
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
