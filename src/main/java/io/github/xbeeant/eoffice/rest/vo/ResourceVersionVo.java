package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.ResourceVersion;

/**
 * @author xiaobiao
 * @version 2022/2/13
 */
public class ResourceVersionVo extends ResourceVersion {
    private String actor;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
