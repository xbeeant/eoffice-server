package io.github.xbeeant.eoffice.service.render.office;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/7/1
 */
public class OnlyOfficeCallback {
    private List<Actions> actions;
    private String changesurl;
    private History history;
    private String key;
    private String lastsave;
    private Boolean notmodified;
    private Integer status;
    private String url;
    private List<String> users;

    public List<Actions> getActions() {
        return actions;
    }

    public void setActions(List<Actions> actions) {
        this.actions = actions;
    }

    public String getChangesurl() {
        return changesurl;
    }

    public void setChangesurl(String changesurl) {
        this.changesurl = changesurl;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLastsave() {
        return lastsave;
    }

    public void setLastsave(String lastsave) {
        this.lastsave = lastsave;
    }

    public Boolean getNotmodified() {
        return notmodified;
    }

    public void setNotmodified(Boolean notmodified) {
        this.notmodified = notmodified;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public static class History {
        private List<Changes> changes;
        private String serverVersion;

        public List<Changes> getChanges() {
            return changes;
        }

        public void setChanges(List<Changes> changes) {
            this.changes = changes;
        }

        public String getServerVersion() {
            return serverVersion;
        }

        public void setServerVersion(String serverVersion) {
            this.serverVersion = serverVersion;
        }

        public static class Changes {
            private String created;
            private User user;

            public static class User {
                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }
        }
    }

    public static class Actions {
        private Integer type;
        private String userid;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
