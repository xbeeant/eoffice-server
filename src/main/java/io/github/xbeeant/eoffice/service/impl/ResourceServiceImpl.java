package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.enums.PermConstant;
import io.github.xbeeant.eoffice.exception.FileSaveFailedException;
import io.github.xbeeant.eoffice.mapper.ResourceMapper;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.po.FullPerm;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.service.IStorageService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private IFolderService folderService;

    @Autowired
    private IPermService permService;

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
    public ApiResponse<List<Resource>> hasPermissionResources(Long fid, String uid) {
        ApiResponse<List<Resource>> result = new ApiResponse<>();

        List<Resource> resources = resourceMapper.hasPermissionResources(fid, uid);
        result.setData(resources);;
        return result;
    }

    @Override
    public ApiResponse<Resource> insert(MultipartFile file, Long fid, String uid) {
        Storage storage;
        try {
            storage = storageService.insert(file, uid);
        } catch (Exception e) {
            throw new FileSaveFailedException();
        }
        // 获取文件夹信息
        ApiResponse<Folder> folderInfo = folderService.selectByPrimaryKey(fid);
        // 写入资源信息
        Resource resource = new Resource();
        resource.setPath(folderInfo.getData().getPath());
        resource.setName(storage.getName());
        resource.setExtension(storage.getExtension());
        resource.setAid(storage.getAid());
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
        perm.setType(PermConstant.RESOURCE);
        permService.insertSelective(perm);

        ApiResponse<Resource> result = new ApiResponse<>();
        result.setData(resource);

        return result;
    }
}