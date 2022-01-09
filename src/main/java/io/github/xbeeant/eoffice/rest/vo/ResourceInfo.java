package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.User;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/1/9
 */
public class ResourceInfo extends Resource {

    private List<User> users;


    /**
     * get field
     *
     * @return users
     */
    public List<User> getUsers() {
        return this.users;
    }

    /**
     * set field
     *
     * @param users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
