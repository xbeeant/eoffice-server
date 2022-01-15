package io.github.xbeeant.eoffice.po;

/**
 * @author xiaobiao
 * @version 2022/1/14
 */
public enum PermTargetType {
    TEAM(1),
    MEMBER(0);

    private Integer type;

    PermTargetType(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
