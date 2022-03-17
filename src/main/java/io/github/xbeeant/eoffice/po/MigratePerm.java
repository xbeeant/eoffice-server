package io.github.xbeeant.eoffice.po;

import io.github.xbeeant.eoffice.model.Perm;

/**
 * @author xiaobiao
 * @version 2022/3/11
 */
public class MigratePerm extends Perm {

    @Override
    public void setDownload(Boolean download) {
        if (null == download || Boolean.TRUE.equals(download)) {
            super.setDownload(download);
        }
    }

    @Override
    public void setEdit(Boolean edit) {
        if (null == edit || Boolean.TRUE.equals(edit)) {
            super.setEdit(edit);
        }
    }

    @Override
    public void setPrint(Boolean print) {
        if (null == print || Boolean.TRUE.equals(print)) {
            super.setPrint(print);
        }
    }

    @Override
    public void setView(Boolean view) {
        if (null == view || Boolean.TRUE.equals(view)) {
            super.setView(view);
        }
    }

    @Override
    public void setComment(Boolean comment) {
        if (null == comment || Boolean.TRUE.equals(comment)) {
            super.setComment(comment);
        }
    }

    @Override
    public void setShare(Boolean share) {
        if (null == share || Boolean.TRUE.equals(share)) {
            super.setShare(share);
        }
    }
}
