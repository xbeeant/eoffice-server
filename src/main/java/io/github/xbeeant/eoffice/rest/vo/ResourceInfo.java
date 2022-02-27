package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/1/9
 */
public class ResourceInfo extends Resource {

    private Storage latest;

    private List<ResourcePerm> permed;

    public List<ResourcePerm> getPermed() {
        return permed;
    }

    public void setPermed(List<ResourcePerm> permed) {
        this.permed = permed;
    }

    public Storage getLatest() {
        return latest;
    }

    public void setLatest(Storage latest) {
        this.latest = latest;
    }
}
