package io.github.xbeeant.eoffice.rest.vo;

/**
 * @author xiaobiao
 * @version 2021/10/31
 */
public class AntCurrentUser {
    /**
     * 名字
     *
     */
    private String name;

    /**
     * 头像
     *
     */
    private String avatar;

    /**
     * 权限（角色）
     *
     */
    private String access;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
