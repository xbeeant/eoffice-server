package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Perm;

/**
 * @author xiaobiao
 * @date 2022/1/11
 */
public class ResourcePerm extends Perm {

    /**
     * 目标名称（昵称、群组名称）
     */
    private String targetName;


    private Integer targetType;

    @Override
    public Integer getTargetType() {
        return targetType;
    }

    @Override
    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
