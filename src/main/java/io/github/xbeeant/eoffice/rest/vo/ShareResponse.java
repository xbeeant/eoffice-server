package io.github.xbeeant.eoffice.rest.vo;

/**
 * @author xiaobiao
 * @version 2022/1/20
 */
public class ShareResponse {
    private String share;

    private Long shareId;

    private String extension;

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }
}
