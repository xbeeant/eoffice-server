package io.github.xbeeant.eoffice.rest.inner;

import io.github.xbeeant.core.BaseModelObject;
import io.github.xbeeant.eoffice.model.Storage;

import java.io.Serializable;

/**
 * 文档
 */
public class Attachment extends BaseModelObject<Long> implements Serializable {

    /**
     * 附件主键
     */
    private Long aid;

    /**
     * 文档名称
     */
    private String filename;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件路径
     */
    private String attachment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table eoffice_attachment
     *
     * @mbg.generated Sat Jul 10 16:33:13 CST 2021
     */
    private static final long serialVersionUID = 1L;

    public static Storage convert(Attachment item) {
        if (item == null) {
            return null;
        }
        Storage result = new Storage();
        result.setSid(item.getAid());
        result.setName(item.getFilename());
        result.setSize(item.getFileSize());
        result.setMd5(item.getMd5());
        result.setExtension(item.getFileType());
        result.setPath(String.valueOf(item.getFilecloudId()));
        result.setCreateAt(item.getCreateAt());
        result.setCreateBy(item.getCreateBy());
        result.setUpdateAt(item.getUpdateAt());
        result.setUpdateBy(item.getUpdateBy());
        return result;
    }

    /**
     * get field 附件主键
     * @return aid 附件主键
     */
    public Long getAid() {
        return aid;
    }

    /**
     * set filed 附件主键
     * @param aid the value for 附件主键
     */
    public void setAid(Long aid) {
        this.aid = aid;
    }

    /**
     * get field 文档名称
     * @return filename 文档名称
     */
    public String getFilename() {
        return filename;
    }

    /**
     * set filed 文档名称
     * @param filename the value for 文档名称
     */
    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    /**
     * get field 文件大小
     * @return fileSize 文件大小
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * set filed 文件大小
     * @param fileSize the value for 文件大小
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * get field 文件MD5
     * @return md5 文件MD5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * set filed 文件MD5
     * @param md5 the value for 文件MD5
     */
    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    /**
     * get field 文件类型
     * @return fileType 文件类型
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * set filed 文件类型
     * @param fileType the value for 文件类型
     */
    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    /**
     * get field 文件路径
     * @return attachment 文件路径
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * set filed 文件路径
     * @param attachment the value for 文件路径
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    @Override
    public Long valueOfKey() {
        return aid;
    }

    @Override
    public void assignKeyValue(Long value) {
        this.aid = value;
    }

    /**
     * eoffice_attachment.filecloud_id
     */
    private Long filecloudId;

    /**
     * get field eoffice_attachment.filecloud_id
     * @return filecloudId eoffice_attachment.filecloud_id
     */
    public Long getFilecloudId() {
        return filecloudId;
    }

    /**
     *    eoffice_attachment.filecloud_id
     *
     * @param filecloudId the value for eoffice_attachment.filecloud_id
     */
    public void setFilecloudId(Long filecloudId) {
        this.filecloudId = filecloudId;
    }
}
