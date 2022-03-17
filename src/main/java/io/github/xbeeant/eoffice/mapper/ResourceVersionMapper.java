package io.github.xbeeant.eoffice.mapper;

import com.github.pagehelper.Page;
import io.github.xbeeant.eoffice.model.ResourceVersion;
import io.github.xbeeant.eoffice.rest.vo.ResourceVersionVo;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mybatis code generator
 * @version Sun Dec 19 22:49:46 CST 2021
 */
@Mapper
public interface ResourceVersionMapper extends IMybatisPageHelperDao<ResourceVersion, Long> {

    /**
     * fuzzy search vo
     *
     * @param example 例子
     * @return {@link Page}
     * @see Page
     * @see ResourceVersionVo
     */
    Page<ResourceVersionVo> fuzzySearchVo(ResourceVersion example);
}
