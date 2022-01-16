package io.github.xbeeant.eoffice.po;

/**
 * @author xiaobiao
 * @date 2022/1/11
 */
public enum PermType {
    /**
     * 文件夹
     */
    FOLDER(10),
    /**
     * 文件夹分享
     */
    SHARE_FOLDER(11),
    /**
     * 文件
     */
    FILE(20),
    /**
     * 文件分享
     */
    SHARE_FILE(21);

    /**
     * 类型
     *
     */
    private Integer type;


    PermType(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
