package io.github.xbeeant.eoffice.model;

import io.github.xbeeant.core.BaseModelObject;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xbeeant mybatis generator
 * @version Wed Jan 12 11:56:33 CST 2022
 * eoffice_share
 */
public class Share extends BaseModelObject<Long> implements Serializable {

    /**
     * eoffice_share.share_id
     */
    private Long shareId;

    /**
     * eoffice_share.target_id
     */
    private Long targetId;

    /**
     * 类型 0 资源 1 文件夹
     */
    private Integer type;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 授权码， 空表示不需要授权码
     */
    private String authCode;

    /**
     * 截止日期， 0000-00-00 00:00:00 表示永久有效
     */
    private Date endtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table eoffice_share
     *
     * @mbg.generated Wed Jan 12 11:56:33 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * get field eoffice_share.share_id
     * @return shareId eoffice_share.share_id
     */
    public Long getShareId() {
        return shareId;
    }

    /**
     *    eoffice_share.share_id
     *
     * @param shareId the value for eoffice_share.share_id
     */
    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    /**
     * get field eoffice_share.target_id
     * @return targetId eoffice_share.target_id
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     *    eoffice_share.target_id
     *
     * @param targetId the value for eoffice_share.target_id
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
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

    /**
     * get field 访问地址
     * @return url 访问地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * set filed 访问地址
     * @param url the value for 访问地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * get field 授权码， 空表示不需要授权码
     * @return authCode 授权码， 空表示不需要授权码
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * set filed 授权码， 空表示不需要授权码
     * @param authCode the value for 授权码， 空表示不需要授权码
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    /**
     * get field 截止日期， 0000-00-00 00:00:00 表示永久有效
     * @return endtime 截止日期， 0000-00-00 00:00:00 表示永久有效
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * set filed 截止日期， 0000-00-00 00:00:00 表示永久有效
     * @param endtime the value for 截止日期， 0000-00-00 00:00:00 表示永久有效
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    @Override
    public Long valueOfKey() {
        return shareId;
    }

    @Override
    public void assignKeyValue(Long value) {
        this.shareId = value;
    }

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * get field 是否删除
     * @return deleted 是否删除
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * set filed 是否删除
     * @param deleted the value for 是否删除
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
