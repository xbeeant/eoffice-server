package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.PermMapper;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * eoffice_perm
 */
@Service
public class PermServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Perm, Long> implements IPermService {

    @Autowired
    private PermMapper permMapper;

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
    public Perm perm(Long targetId, Long rid, PermType type) {
        Perm perm = permMapper.perm(targetId, rid, type.getType());
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
    public ApiResponse<String> perm(List<Long> targetIds, List<String> perm, Long rid, PermType type) {

        // 获取已有的用户ID，权限进行权限重写
        permMapper.removeExists(targetIds, rid);

        List<Perm> inserts = new ArrayList<>();

        for (Long uid : targetIds) {
            inserts.add(new Perm(perm, rid, uid, type.getType()));
        }

        // 写入新的授权
        if (!CollectionUtils.isEmpty(inserts)) {
            batchInsertSelective(inserts);
        }

        return new ApiResponse<>();
    }
}
