package io.github.xbeeant.eoffice.service.impl;

import com.github.pagehelper.page.PageMethod;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.enums.PermTypeConstant;
import io.github.xbeeant.eoffice.exception.FileSaveFailedException;
import io.github.xbeeant.eoffice.exception.ResourceMissingException;
import io.github.xbeeant.eoffice.mapper.ResourceMapper;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.po.FullPerm;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.eoffice.service.*;
import io.github.xbeeant.eoffice.service.storage.StorageFactory;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
        ResourceVo resourceVo = new ResourceVo();
        BeanUtils.copyProperties(resourceApiResponse.getData(), resourceVo);
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
        if (!StringUtils.isEmpty(pageBounds.getOrders())) {
            PageMethod.orderBy(pageBounds.getOrders());
        } else {
            PageMethod.orderBy("create_at desc");
        }
        List<Resource> resources = resourceMapper.hasPermissionResources(fid, uid);
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
    public ApiResponse<Resource> upload(MultipartFile file, Long fid, String uid) {
        Storage storage;
        try {
            storage = storageService.insert(file, uid);
        } catch (Exception e) {
            throw new FileSaveFailedException();
        }

        Resource resource = saveResource(fid, uid, storage);
        ApiResponse<Resource> result = new ApiResponse<>();
        result.setData(resource);
        return result;
    }

    private ResourceVo saveResource(Long fid, String uid, Storage storage) {
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
        perm.setTargetId(resource.getRid());
        perm.setUid(Long.valueOf(uid));
        perm.setType(PermTypeConstant.RESOURCE);
        permService.insertSelective(perm);

        resource.setPerm(perm);
        return resource;
    }

    @Override
    public Perm permission(Long rid, Long userId) {
        return permService.perm(rid, userId, 0);
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
}
