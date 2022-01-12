package io.github.xbeeant.eoffice.mapper;

import com.github.pagehelper.Page;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.rest.vo.ShareResourceVo;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mybatis code generator
 * @version Wed Jan 12 11:56:33 CST 2022
 */
@Mapper
public interface ShareMapper extends IMybatisPageHelperDao<Share, Long> {

    /**
     * 列表
     *
     * @param userId 用户标识
     * @return {@link Page}
     * @see Page
     * @see ShareResourceVo
     */
    Page<ShareResourceVo> sharedToUser(String userId);

    /**
     * myshare
     *
     * @param userId 用户标识
     * @return {@link Page}
     * @see Page
     * @see ShareResourceVo
     */
    Page<ShareResourceVo> myshare(@Param("userId") String userId);
}
