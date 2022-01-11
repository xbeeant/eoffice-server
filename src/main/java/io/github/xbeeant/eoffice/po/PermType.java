package io.github.xbeeant.eoffice.po;

/**
 * @author xiaobiao
 * @date 2022/1/11
 */
public enum PermType {
    FOLDER(1),
    FILE(0);

    private Integer type;


    PermType(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
