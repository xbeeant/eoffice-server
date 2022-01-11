package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.User;

/**
 * @author xiaobiao
 * @date 2022/1/11
 */
public class ResourcePerm extends Perm {

    /**
     * 用户名
     *
     */
    private String username;

    /**
     * 昵称
     *
     */
    private String nickname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
