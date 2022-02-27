package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.model.User;

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

    private User owner;

    /**
     * 权限
     *
     */
    private Perm perm;

    private Storage storage;

    private Object extra;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Perm getPerm() {
        return perm;
    }

    public void setPerm(Perm perm) {
        this.perm = perm;
    }
}
