package io.github.xbeeant.eoffice.service.render.office;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/7/13
 */
public class OfficeHistory {


    private String currentVersion;
    private List<History> history = new ArrayList<>();

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public static class History {
        private List<Changes> changes;
        private Date created;
        private String key;
        private String serverVersion;
        private User user;
        private Integer version;

        public List<Changes> getChanges() {
            return changes;
        }

        public void setChanges(List<Changes> changes) {
            this.changes = changes;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getServerVersion() {
            return serverVersion;
        }

        public void setServerVersion(String serverVersion) {
            this.serverVersion = serverVersion;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public static class Changes {
            private String created;
            private OnlyOfficeCallback.History.Changes.User user;

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public OnlyOfficeCallback.History.Changes.User getUser() {
                return user;
            }

            public void setUser(OnlyOfficeCallback.History.Changes.User user) {
                this.user = user;
            }

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
        }

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
    }
}
