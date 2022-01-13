package io.github.xbeeant.eoffice.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.github.xbeeant.core.BaseModelObject;
import java.io.Serializable;

/**
 * 
 * eoffice_user
 */
public class User extends BaseModelObject<Long> implements Serializable {
    /**
     * 
     * 用户ID
     */
    private Long uid;

    /**
     * 
     * 用户邮箱
     */
    @JSONField(serialize = false)
    private String email;

    /**
     * 
     * 用户手机
     */
    @JSONField(serialize = false)
    private String phone;

    /**
     * 
     * 用户昵称
     */
    private String nickname;

    /**
     * 
     * 用户名
     */
    private String username;

    /**
     * 
     * 用户密码
     */
    @JSONField(serialize = false)
    private String password;

    /**
     * 
     * 用户状态 0 可用 1禁用
     */
    private Boolean status;

    /**
     * 
     * 注册的IP
     */
    @JSONField(serialize = false)
    private String regip;

    /**
     * 
     * 认证方式
     */
    @JSONField(serialize = false)
    private String authType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table eoffice_user
     *
     * @mbg.generated Sat Oct 30 17:59:08 CST 2021
     */
    private static final long serialVersionUID = 1L;

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
     * get field 用户邮箱
     * @return email 用户邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * set filed 用户邮箱
     * @param email the value for 用户邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * get field 用户手机
     * @return phone 用户手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * set filed 用户手机
     * @param phone the value for 用户手机
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * get field 用户昵称
     * @return nickname 用户昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * set filed 用户昵称
     * @param nickname the value for 用户昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * get field 用户名
     * @return username 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * set filed 用户名
     * @param username the value for 用户名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * get field 用户密码
     * @return password 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * set filed 用户密码
     * @param password the value for 用户密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * get field 用户状态 0 可用 1禁用
     * @return status 用户状态 0 可用 1禁用
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * set filed 用户状态 0 可用 1禁用
     * @param status the value for 用户状态 0 可用 1禁用
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * get field 注册的IP
     * @return regip 注册的IP
     */
    public String getRegip() {
        return regip;
    }

    /**
     * set filed 注册的IP
     * @param regip the value for 注册的IP
     */
    public void setRegip(String regip) {
        this.regip = regip == null ? null : regip.trim();
    }

    /**
     * get field 认证方式
     * @return authType 认证方式
     */
    public String getAuthType() {
        return authType;
    }

    /**
     * set filed 认证方式
     * @param authType the value for 认证方式
     */
    public void setAuthType(String authType) {
        this.authType = authType == null ? null : authType.trim();
    }

    @Override
    public Long valueOfKey() {
        return uid;
    }

    @Override
    public void assignKeyValue(Long value) {
        this.uid = value;
    }
}