package io.github.xbeeant.eoffice.rest.vo;

/**
 * @author xiaobiao
 * @version 2021/11/6
 */
public class Breadcrumb {
    /**
     * breadcrumb name
     *
     */
    private String breadcrumbName;

    /**
     * 路径
     *
     */
    private String path;

    public Breadcrumb(String name, Long fid) {
        this.breadcrumbName = name;
        this.path = "/res/" + fid;
    }


    public String getBreadcrumbName() {
        return breadcrumbName;
    }

    public void setBreadcrumbName(String breadcrumbName) {
        this.breadcrumbName = breadcrumbName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
