package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.eoffice.model.Config;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;

/**
 * @author mybatis code generator
 * @version Mon Nov 22 22:00:22 CST 2021
 */
public interface IConfigService extends IMybatisPageHelperService<Config, Long> {
    /**
     * value of
     *
     * @param module 模块
     * @param ckey   ckey
     * @return {@link String}
     * @see String
     */
    String valueOf(String module, String ckey);
}