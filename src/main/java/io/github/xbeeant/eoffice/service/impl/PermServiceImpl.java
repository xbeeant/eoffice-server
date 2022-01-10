package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.PermMapper;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Perm perm(Long targetId, Long uid, int type) {
        Perm perm = permMapper.perm(targetId, uid, type);
        if (null != perm) {
            return perm;
        }

        return new Perm();
    }

    @Override
    public List<User> users(Long targetId) {
        return permMapper.users(targetId);
    }

    @Override
    public ApiResponse<String> perm(List<Long> users, List<String> perm, Long targetId, Integer type, String actorId) {

        // 获取已有的用户ID，权限进行权限重写
        List<Perm> exists = permMapper.exists(users, targetId);

        Map<Long, Perm> map = new HashMap<>(exists.size());

        for (Perm item : exists) {
            map.put(item.getUid(), item);
        }

        List<Perm> updates = new ArrayList<>();

        List<Perm> inserts = new ArrayList<>();

        for (Long uid : users) {
            Perm exist = map.get(uid);
            if (null != exist) {
                Perm update = new Perm(perm, uid, exist.getTargetId(), type);
                update.setPid(exist.getPid());
                updates.add(update);
            } else {
                inserts.add(new Perm(perm, uid, targetId, type));
            }
        }

        // 写入新的授权
        if (!CollectionUtils.isEmpty(inserts)) {
            batchInsertSelective(inserts);
        }

        // 更新已有的授权
        if (!CollectionUtils.isEmpty(inserts)) {
            batchUpdateByPrimaryKeySelective(updates);
        }

        return new ApiResponse<>();
    }
}
