package io.github.xbeeant.eoffice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.core.RandomHelper;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.ShareMapper;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.model.ShareRange;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.eoffice.rest.vo.ShareResourceVo;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.service.IShareRangeService;
import io.github.xbeeant.eoffice.service.IShareService;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
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
    public ApiResponse<Share> share(List<Long> users, List<String> perm, Long rid, Date endtime) {
        ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(rid);
        if (!resourceApiResponse.getSuccess()) {
            ApiResponse<Share> result = new ApiResponse<>();
            result.setResult(ErrorCodeConstant.NO_MATCH, "资源已被删除");
            return result;
        }
        permService.perm(users, perm, rid, PermType.SHARE_FOLDER);
        // 记录分享操作
        Share share = new Share();
        share.setAuthCode(RandomHelper.random(4));
        share.setTargetId(rid);
        share.setEndtime(endtime);
        // todo set url
        share.setType("folder".equals(resourceApiResponse.getData()) ? PermType.SHARE_FOLDER.getType() : PermType.SHARE_FILE.getType());
        insertSelective(share);
        // 记录分享的目标群体
        List<ShareRange> shareRanges = new ArrayList<>(users.size());
        ShareRange shareRange;
        for (Long uid : users) {
            shareRange = new ShareRange();
            shareRange.setTargetId(uid);
            shareRange.setType(PermType.SHARE_FOLDER.getType());
            shareRanges.add(shareRange);
        }
        shareRangeService.batchInsertSelective(shareRanges);
        return new ApiResponse<>();
    }
}
