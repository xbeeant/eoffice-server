package io.github.xbeeant.eoffice.model;

import io.github.xbeeant.core.BaseModelObject;
import java.io.Serializable;

/**
 * 
 * @author xbeeant mybatis generator
 * @version Thu Jan 13 09:18:55 CST 2022
 * eoffice_group
 */
public class Group extends BaseModelObject<Long> implements Serializable {
    /**
     * 
     * 
     * 分组ID
     */
    private Long gid;

    /**
     * 
     * 
     * 分组名称
     */
    private String name;

    /**
     * 
     * 
     * 父分组ID
     */
    private Long pgid;

    /**
     * 
     * 
     * 0 系统分组 1 用户自定义分组
     */
    private Integer type;

    /**
     * 
     * 
     * 外部系统的ID
     */
    private String extraId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table eoffice_group
     *
     * @mbg.generated Thu Jan 13 09:18:55 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * get field 分组ID
     * @return gid 分组ID
     */
    public Long getGid() {
        return gid;
    }

    /**
     * set filed 分组ID
     * @param gid the value for 分组ID
     */
    public void setGid(Long gid) {
        this.gid = gid;
    }

    /**
     * get field 分组名称
     * @return name 分组名称
     */
    public String getName() {
        return name;
    }

    /**
     * set filed 分组名称
     * @param name the value for 分组名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * get field 父分组ID
     * @return pgid 父分组ID
     */
    public Long getPgid() {
        return pgid;
    }

    /**
     * set filed 父分组ID
     * @param pgid the value for 父分组ID
     */
    public void setPgid(Long pgid) {
        this.pgid = pgid;
    }

    /**
     * get field 0 系统分组 1 用户自定义分组
     * @return type 0 系统分组 1 用户自定义分组
     */
    public Integer getType() {
        return type;
    }

    /**
     * set filed 0 系统分组 1 用户自定义分组
     * @param type the value for 0 系统分组 1 用户自定义分组
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * get field 外部系统的ID
     * @return extraId 外部系统的ID
     */
    public String getExtraId() {
        return extraId;
    }

    /**
     * set filed 外部系统的ID
     * @param extraId the value for 外部系统的ID
     */
    public void setExtraId(String extraId) {
        this.extraId = extraId == null ? null : extraId.trim();
    }

    @Override
    public Long valueOfKey() {
        return gid;
    }

    @Override
    public void assignKeyValue(Long value) {
        this.gid = value;
    }
}