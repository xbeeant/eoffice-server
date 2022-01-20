package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.PermMapper;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.service.IShareService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * eoffice_perm
 */
@Service
public class PermServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Perm, Long> implements IPermService {

    @Autowired
    private PermMapper permMapper;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IShareService shareService;

    @Autowired
    private IFolderService folderService;

    @Override
    public IMybatisPageHelperDao<Perm, Long> getRepositoryDao() {
        return this.permMapper;
    }

    @Override
    public void setDefaults(Perm record) {
        if (record.getPid() == null) {
            record.setPid(IdWorker.getId());
        }
    }

    @Override
    public Perm perm(Long targetId, Long rid) {
        ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(rid);
        boolean isFolder = "folder".equals(resourceApiResponse.getData().getExtension());
        PermType type = PermType.FILE;
        if (isFolder) {
            type = PermType.FOLDER;
        }

        // 资源的权限
        Perm perm = permMapper.perm(rid, targetId, type.getType());
        if (null != perm) {
            return perm;
        }

        // 资源对应的用户分组的权限
        perm = permMapper.permGroup(rid, targetId, type.getType());
        if (null != perm) {
            return perm;
        }

        // 资源对应的文件夹的权限
        List<Folder> folders = folderService.parentFolders(resourceApiResponse.getData().getFid());
        for (Folder folder : folders) {
            perm = permMapper.perm(folder.getFid(), targetId, PermType.FOLDER.getType());
            if (null != perm) {
                return perm;
            }

            perm = permMapper.permGroup(folder.getFid(), targetId, PermType.FOLDER.getType());
            if (null != perm) {
                return perm;
            }
        }



        return new Perm();
    }

    @Override
    public Perm sharePerm(Long userId, Long rid, Share share) {
        // 是否已经过期
        if (null != share.getEndtime() && share.getEndtime().before(new Date())) {
            return new Perm();
        }

        ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(share.getTargetId());
        boolean isFolder = "folder".equals(resourceApiResponse.getData().getExtension());
        PermType type = PermType.SHARE_FILE;
        if (isFolder) {
            type = PermType.SHARE_FOLDER;
        }

        Perm perm = permMapper.sharePerm(rid, userId, type.getType());
        if (null != perm) {
            return perm;
        }

        // 资源没有对用户进行授权，校验资源有没有对用户所在的组进行授权
        perm = permMapper.sharePermGroup(rid, share.getTargetId(), type.getType());
        if (null != perm) {
            return perm;
        }

        return new Perm();
    }

    @Override
    public List<User> users(Long uid) {
        return permMapper.users(uid);
    }

    @Override
    public ApiResponse<String> perm(List<Long> targetIds, List<String> perm, Resource resource, PermType type, PermTargetType targetType) {
        targetIds.removeIf(target -> target.equals(Long.valueOf(resource.getCreateBy())));
        if (CollectionUtils.isEmpty(targetIds)) {
            return new ApiResponse<>();
        }

        // 获取已有的用户ID，权限进行权限重写
        permMapper.removeExists(targetIds, resource.getRid());

        List<Perm> inserts = new ArrayList<>();

        for (Long uid : targetIds) {
            // 不能修改自己的权限
            inserts.add(new Perm(perm, resource.getRid(), uid, type, targetType));
        }

        // 写入新的授权
        if (!CollectionUtils.isEmpty(inserts)) {
            batchInsertSelective(inserts);
        }

        return new ApiResponse<>();
    }
}
