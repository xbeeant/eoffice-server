package io.github.xbeeant.eoffice.rest.inner;

import io.github.xbeeant.core.BaseModelObject;

import java.io.Serializable;

public abstract class AbstractPerm extends BaseModelObject<Long> implements Serializable {

    /**
     * 注释权限
     */
    private Boolean comment;

    /**
     * 拷贝权限
     */
    private Boolean copy;

    /**
     * 删除注释权限
     */
    private Boolean deleteCommentAuthorOnly;

    /**
     * 下载权限
     */
    private Boolean download;

    /**
     * 编辑权限
     */
    private Boolean edit;

    /**
     * 编辑注释权限
     */
    private Boolean editCommentAuthorOnly;

    /**
     * eoffice_folder_user.fill_forms
     */
    private Boolean fillForms;

    /**
     * eoffice_folder_user.modify_content_control
     */
    private Boolean modifyContentControl;

    /**
     * eoffice_folder_user.modify_filter
     */
    private Boolean modifyFilter;

    /**
     * eoffice_folder_user.print
     */
    private Boolean print;

    /**
     * eoffice_folder_user.review
     */
    private Boolean review;

    public boolean hasPermission() {
        return Boolean.TRUE.equals(getDownload()) || Boolean.TRUE.equals(getEdit()) || Boolean.TRUE.equals(getPrint()) || Boolean.TRUE.equals(getReview());
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public Boolean getCopy() {
        // 不能编辑，只能查看的权限，不允许copy
        if (Boolean.FALSE.equals(edit)) {
            return false;
        }
        return copy;
    }

    public void setCopy(Boolean copy) {
        this.copy = copy;
    }

    public Boolean getDeleteCommentAuthorOnly() {
        return deleteCommentAuthorOnly;
    }

    public void setDeleteCommentAuthorOnly(Boolean deleteCommentAuthorOnly) {
        this.deleteCommentAuthorOnly = deleteCommentAuthorOnly;
    }

    public Boolean getDownload() {
        return download;
    }

    public void setDownload(Boolean download) {
        this.download = download;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Boolean getEditCommentAuthorOnly() {
        return editCommentAuthorOnly;
    }

    public void setEditCommentAuthorOnly(Boolean editCommentAuthorOnly) {
        this.editCommentAuthorOnly = editCommentAuthorOnly;
    }

    public Boolean getFillForms() {
        return fillForms;
    }

    public void setFillForms(Boolean fillForms) {
        this.fillForms = fillForms;
    }

    public Boolean getModifyContentControl() {
        return modifyContentControl;
    }

    public void setModifyContentControl(Boolean modifyContentControl) {
        this.modifyContentControl = modifyContentControl;
    }

    public Boolean getModifyFilter() {
        return modifyFilter;
    }

    public void setModifyFilter(Boolean modifyFilter) {
        this.modifyFilter = modifyFilter;
    }

    public Boolean getPrint() {
        return print;
    }

    public void setPrint(Boolean print) {
        this.print = print;
    }

    public Boolean getReview() {
        return review;
    }

    public void setReview(Boolean review) {
        this.review = review;
    }
}
