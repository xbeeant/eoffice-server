package io.github.xbeeant.antdesign;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/7/6
 */
public class TreeNode {
    private List<TreeNode> children = new ArrayList<>();

    private String code;

    private String extra;

    private Boolean isLeaf;

    private String label;

    @JSONField(name = "value")
    private Long key;

    private Long pKey;

    @JSONField(serialize = false)
    private boolean parentExist = false;

    private String title;

    public void addChildren(TreeNode node) {
        if (null == children) {
            children = new ArrayList<>();
        }
        children.add(node);
    }

    public String getLabel() {
        return title;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    @JsonProperty("isLeaf")
    public Boolean getLeaf() {
        return CollectionUtils.isEmpty(children);
    }

    public void setParentExist(boolean parentExist) {
        this.parentExist = parentExist;
    }

    public boolean isParentExist() {
        return parentExist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getpKey() {
        return pKey;
    }

    public void setpKey(Long pKey) {
        this.pKey = pKey;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
