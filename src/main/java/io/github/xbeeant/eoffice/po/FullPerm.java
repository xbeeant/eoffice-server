package io.github.xbeeant.eoffice.po;

import io.github.xbeeant.eoffice.model.Perm;

/**
 * @author xiaobiao
 * @version 2021/11/3
 */
public class FullPerm extends Perm {

    @Override
    public Boolean getDownload() {
        return true;
    }

    @Override
    public Boolean getEdit() {
        return true;
    }

    @Override
    public Boolean getPrint() {
        return true;
    }

    @Override
    public Boolean getView() {
        return true;
    }

    @Override
    public Boolean getComment() {
        return true;
    }

    @Override
    public Boolean getShare() {
        return true;
    }
}
