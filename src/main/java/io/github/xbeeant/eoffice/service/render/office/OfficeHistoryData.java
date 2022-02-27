package io.github.xbeeant.eoffice.service.render.office;

/**
 * @author xiaobiao
 * @version 2021/7/14
 */
public class OfficeHistoryData {
    private String changesUrl;
    private String key;
    private Previous previous;
    private String url;
    private Integer version;

    public String getChangesUrl() {
        return changesUrl;
    }

    public String getKey() {
        return key;
    }

    public Previous getPrevious() {
        return previous;
    }

    public String getUrl() {
        return url;
    }

    public Integer getVersion() {
        return version;
    }

    public void setChangesUrl(String changesUrl) {
        this.changesUrl = changesUrl;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPrevious(Previous previous) {
        this.previous = previous;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static class Previous {
        private String key;
        private String url;

        public String getKey() {
            return key;
        }

        public String getUrl() {
            return url;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
