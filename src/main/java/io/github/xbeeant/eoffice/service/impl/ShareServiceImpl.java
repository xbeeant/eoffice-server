package io.github.xbeeant.eoffice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.core.RandomHelper;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ShareMapper;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.model.ShareRange;
import io.github.xbeeant.eoffice.po.PermTargetType;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.eoffice.rest.vo.ShareResourceVo;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.service.IShareRangeService;
import io.github.xbeeant.eoffice.service.IShareService;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xbeeant mybatis generator
 * @version Wed Jan 12 11:56:33 CST 2022
 * eoffice_share
 */
@Service
public class ShareServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Share, Long> implements IShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IShareRangeService shareRangeService;

    @Autowired
    private IPermService permService;

    @Override
    public IMybatisPageHelperDao<Share, Long> getRepositoryDao() {
        return this.shareMapper;
    }

    @Override
    public void setDefaults(Share share) {
        if (share.getShareId() == null) {
            share.setShareId(IdWorker.getId());
        }
    }

    @Override
    public ApiResponse<PageResponse<ShareResourceVo>> list(String userId, PageBounds pageBounds) {
        ApiResponse<PageResponse<ShareResourceVo>> apiResponse = new ApiResponse<>();
        PageMethod.orderBy(pageBounds.getOrders());
        PageMethod.startPage(pageBounds.getPage(), pageBounds.getLimit());
        Page<ShareResourceVo> result = shareMapper.sharedToUser(userId);
        if (result.isEmpty()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }
        PageResponse<ShareResourceVo> pageList = new PageResponse<>(result, result.getTotal(), pageBounds.getPage());
        apiResponse.setData(pageList);
        return apiResponse;
    }

    @Override
    public ApiResponse<Share> share(List<Long> users, List<String> perm, Long rid, PermTargetType targetType, Date endtime) {
        ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(rid);
        if (!resourceApiResponse.getSuccess()) {
            ApiResponse<Share> result = new ApiResponse<>();
            result.setResult(ErrorCodeConstant.NO_MATCH, "资源已被删除");
            return result;
        }

        Resource resource = resourceApiResponse.getData();

        boolean isFolder = "folder".equals(resource.getExtension());
        PermType permType = PermType.SHARE_FILE;
        if (isFolder) {
            permType = PermType.SHARE_FOLDER;
        }
        permService.perm(users, perm, resource, permType, targetType);
        // 记录分享操作
        Share share = new Share();
        share.setAuthCode(RandomHelper.random(4));
        share.setTargetId(rid);
        share.setEndtime(endtime);
        // todo set url
        share.setType(permType.getType());
        insertSelective(share);
        // 记录分享的目标群体
        List<ShareRange> shareRanges = new ArrayList<>(users.size());
        ShareRange shareRange;
        for (Long uid : users) {
            shareRange = new ShareRange();
            shareRange.setShareId(share.getShareId());
            shareRange.setTargetId(uid);
            shareRange.setType(permType.getType());
            shareRanges.add(shareRange);
        }
        shareRangeService.batchInsertSelective(shareRanges);
        return new ApiResponse<>();
    }

    @Override
    public ApiResponse<PageResponse<ShareResourceVo>> myshare(String userId, PageBounds pageBounds) {
        ApiResponse<PageResponse<ShareResourceVo>> apiResponse = new ApiResponse<>();
        PageMethod.orderBy(pageBounds.getOrders());
        PageMethod.startPage(pageBounds.getPage(), pageBounds.getLimit());
        Page<ShareResourceVo> result = shareMapper.myshare(userId);
        if (result.isEmpty()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }
        PageResponse<ShareResourceVo> pageList = new PageResponse<>(result, result.getTotal(), pageBounds.getPage());
        apiResponse.setData(pageList);
        return apiResponse;
    }

    @Override
    public boolean isShared(Resource resource, Long targetId) {
        // 个人关系
        List<Long> shared = shareMapper.isShared(resource.getRid(), targetId);
        if (!CollectionUtils.isEmpty(shared)) {
            return true;
        }

        // 团队关系
        shared = shareMapper.isTeamShared(resource.getRid(), targetId);
        if (!CollectionUtils.isEmpty(shared)) {
            return true;
        }

        return false;
    }

    @Override
    public ApiResponse<Resource> avaliable(Long shareId, String authCode, String userId) {
        ApiResponse<Resource> response = new ApiResponse<>();
        ApiResponse<Share> shareApiResponse = selectByPrimaryKey(shareId);
        if (!shareApiResponse.getSuccess()) {
            return response;
        }


        Share share = shareApiResponse.getData();
        // 提取码 校验
        if (!share.getAuthCode().equals(authCode)) {
            response.setResult(101, "提取码错误");
            return response;
        }

        // 是否已经过期
        if (null != share.getEndtime() && share.getEndtime().before(new Date())) {
            response.setResult(100, "分享已过期");
            return response;
        }

        // 获取资源
        ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(share.getTargetId());
        Resource resource = resourceApiResponse.getData();

        Perm permission = resourceService.sharePermission(resource.getRid(), Long.valueOf(userId), share);
        if (!permission.hasPermission()) {
            response.setResult(102, "作者已回收所有权限");
            return response;
        }
        response.setData(resource);

        return response;
    }
}
