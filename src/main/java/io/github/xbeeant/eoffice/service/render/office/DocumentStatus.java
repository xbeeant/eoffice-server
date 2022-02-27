package io.github.xbeeant.eoffice.service.render.office;

/**
 * @author xiaobiao
 * @version 2021/7/1
 */
public enum DocumentStatus {
    NOT_FOUND(0, "找不到带有密钥标识符的文档"),

    BEING_EDITED(1, "正在编辑文档"),

    READY_FOR_SAVING(2, "文件已准备好保存"),

    SAVING_ERROR(3, "发生了文档保存错误"),

    CLOSED_WITH_NO_CHANGES(4, "文件关闭，没有变化"),

    BEING_EDITED_STATE_SAVED(6, "正在编辑文档，但保存当前文档状态"),

    FORCE_SAVING_ERROR(7, "强制保存文档时发生错误");
    /**
     * 编码
     */
    private int code;
    /**
     * 异常消息
     */
    private String msg;

    DocumentStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
