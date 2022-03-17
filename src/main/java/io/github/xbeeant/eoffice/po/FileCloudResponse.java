package io.github.xbeeant.eoffice.po;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaobiao
 * @version 2022/2/14
 */
public class FileCloudResponse {

    /**
     * extension
     */
    @JSONField(name = "extension")
    private String extension;
    /**
     * fid
     */
    @JSONField(name = "fid")
    private String fid;
    /**
     * fileSize
     */
    @JSONField(name = "fileSize")
    private Long fileSize;
    /**
     * md5
     */
    @JSONField(name = "md5")
    private String md5;
    /**
     * name
     */
    @JSONField(name = "name")
    private String name;
    /**
     * sha256
     */
    @JSONField(name = "sha256")
    private String sha256;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }
}
