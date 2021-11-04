package io.github.xbeeant.eoffice.model;

import io.github.xbeeant.core.BaseModelObject;
import java.io.Serializable;

/**
 * eoffice_perm
 */
public class Perm extends BaseModelObject<Long> implements Serializable {

    /**
     * ID
     */
    private Long pid;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 下载
     */
    private Boolean download;

    /**
     * 编辑
     */
    private Boolean edit;

    /**
     * 打印
     */
    private Boolean print;

    /**
     * 查看
     */
    private Boolean view;

    /**
     * 是否删除
     */
    private Boolean comment;

    /**
     * 分享
     */
    private Boolean share;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table eoffice_perm
     *
     * @mbg.generated Sat Oct 30 18:44:43 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * get field ID
     * @return pid ID
     */
    public Long getPid() {
        return pid;
    }

    /**
     * set filed ID
     * @param pid the value for ID
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * get field 目标ID
     * @return targetId 目标ID
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * set filed 目标ID
     * @param targetId the value for 目标ID
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /**
     * get field 下载
     * @return download 下载
     */
    public Boolean getDownload() {
        return download;
    }

    /**
     * set filed 下载
     * @param download the value for 下载
     */
    public void setDownload(Boolean download) {
        this.download = download;
    }

    /**
     * get field 编辑
     * @return edit 编辑
     */
    public Boolean getEdit() {
        return edit;
    }

    /**
     * set filed 编辑
     * @param edit the value for 编辑
     */
    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    /**
     * get field 打印
     * @return print 打印
     */
    public Boolean getPrint() {
        return print;
    }

    /**
     * set filed 打印
     * @param print the value for 打印
     */
    public void setPrint(Boolean print) {
        this.print = print;
    }

    /**
     * get field 查看
     * @return view 查看
     */
    public Boolean getView() {
        return view;
    }

    /**
     * set filed 查看
     * @param view the value for 查看
     */
    public void setView(Boolean view) {
        this.view = view;
    }

    /**
     * get field 是否删除
     * @return comment 是否删除
     */
    public Boolean getComment() {
        return comment;
    }

    /**
     * set filed 是否删除
     * @param comment the value for 是否删除
     */
    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    /**
     * get field 分享
     * @return share 分享
     */
    public Boolean getShare() {
        return share;
    }

    /**
     * set filed 分享
     * @param share the value for 分享
     */
    public void setShare(Boolean share) {
        this.share = share;
    }

    @Override
    public Long valueOfKey() {
        return pid;
    }

    @Override
    public void assignKeyValue(Long value) {
        this.pid = value;
    }

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 类型 0 资源 1 文件夹
     */
    private Integer type;

    /**
     * get field 用户ID
     * @return uid 用户ID
     */
    public Long getUid() {
        return uid;
    }

    /**
     * set filed 用户ID
     * @param uid the value for 用户ID
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * get field 类型 0 资源 1 文件夹
     * @return type 类型 0 资源 1 文件夹
     */
    public Integer getType() {
        return type;
    }

    /**
     * set filed 类型 0 资源 1 文件夹
     * @param type the value for 类型 0 资源 1 文件夹
     */
    public void setType(Integer type) {
        this.type = type;
    }
}
