package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.PermMapper;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.po.PermType;
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

        // 文件的权限（自己创建的）
        Perm perm = permMapper.perm(rid, targetId, type.getType());
        if (null != perm) {
            return perm;
        }

        // 资源没有对用户进行授权，校验资源有没有对用户所在的组进行授权
        perm = permMapper.permGroup(rid, targetId, type.getType());
        if (null != perm) {
            return perm;
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
    public ApiResponse<String> perm(List<Long> targetIds, List<String> perm, Long rid, PermType type, PermTargetType targetType) {

        // 获取已有的用户ID，权限进行权限重写
        permMapper.removeExists(targetIds, rid);

        List<Perm> inserts = new ArrayList<>();

        for (Long uid : targetIds) {
            inserts.add(new Perm(perm, rid, uid, type, targetType));
        }

        // 写入新的授权
        if (!CollectionUtils.isEmpty(inserts)) {
            batchInsertSelective(inserts);
        }

        return new ApiResponse<>();
    }
}
