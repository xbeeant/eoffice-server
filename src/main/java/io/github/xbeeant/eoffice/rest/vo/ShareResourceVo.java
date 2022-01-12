package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Share;

/**
 * @author xiaobiao
 * @date 2022/1/12
 */
public class ShareResourceVo extends Share {
    /**
     * 用户
     *
     */
    private ResourceVo resource;

    public ResourceVo getResource() {
        return resource;
    }

    public void setResource(ResourceVo resource) {
        this.resource = resource;
    }
}
