package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Resource;

/**
 * @author xiaobiao
 * @version 2021/11/22
 */
public class ResourceVo extends Resource {
    /**
     * url
     *
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
