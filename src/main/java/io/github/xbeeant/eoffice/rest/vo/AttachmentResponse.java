package io.github.xbeeant.eoffice.rest.vo;

public class AttachmentResponse {
    /**
     * 成功  0 表示上传失败，1 表示上传成功
     */
    private boolean success = false;

    /**
     * 提示的信息，上传成功或上传失败及错误信息等。
     */
    private String message;


    /**
     * 访问地址
     */
    private String url;


    /**
     * get field
     *
     * @return success
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * set field
     *
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * get field
     *
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * set field
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * get field
     *
     * @return url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * set field
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
