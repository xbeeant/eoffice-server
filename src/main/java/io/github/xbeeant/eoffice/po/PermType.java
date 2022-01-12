package io.github.xbeeant.eoffice.po;

/**
 * @author xiaobiao
 * @date 2022/1/11
 */
public enum PermType {
    FOLDER(10),
    SHARE_FOLDER(11),
    FILE(20),
    SHARE_FILE(21);

    private Integer type;


    PermType(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
