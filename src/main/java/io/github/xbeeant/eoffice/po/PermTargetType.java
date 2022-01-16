package io.github.xbeeant.eoffice.po;

/**
 * @author xiaobiao
 * @version 2022/1/14
 */
public enum PermTargetType {
    /**
     * 按团队分享
     */
    TEAM(1),
    /**
     * 按成员分享
     */
    MEMBER(0);

    /**
     * 类型
     *
     */
    private Integer type;

    PermTargetType(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
