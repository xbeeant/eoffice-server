package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.rest.vo.ShareResourceVo;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.mybatis.pagehelper.PageBounds;
import java.util.Date;
import java.util.List;

/**
 * @author mybatis code generator
 * @version Wed Jan 12 11:56:33 CST 2022
 */
public interface IShareService extends IMybatisPageHelperService<Share, Long> {

    /**
     * 列表
     *
     * @param userId     用户标识
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<ShareResourceVo>> list(String userId, PageBounds pageBounds);

    /**
     * 分享
     *
     * @param users   用户
     * @param perm    权限
     * @param rid     资源ID
     * @param endtime
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see Share
     */
    ApiResponse<Share> share(List<Long> users, List<String> perm, Long rid, Date endtime);

    /**
     * myshare
     *
     * @param userId 用户标识
     * @param pageBounds pageBounds
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see PageResponse
     */
    ApiResponse<PageResponse<ShareResourceVo>> myshare(String userId, PageBounds pageBounds);

}
