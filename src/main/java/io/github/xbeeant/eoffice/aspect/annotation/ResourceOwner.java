package io.github.xbeeant.eoffice.aspect.annotation;

import io.github.xbeeant.core.service.IAbstractService;

import java.lang.annotation.*;

/**
 * @author xiaobiao
 * @version 2022/1/18
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceOwner {

    /**
     * 主键
     *
     * @return {@link String}
     * @see String
     */
    String id();

    /**
     * 通过主键查询的服务
     *
     * @return {@link IAbstractService}
     */
    Class<?> selectService();
}
