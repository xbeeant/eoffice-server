package io.github.xbeeant.eoffice.rest.vo;

import io.github.xbeeant.eoffice.model.Slave;

/**
 * @author xiaobiao
 * @version 2022/2/21
 */
public class QiankunSlave {

    /**
     * name
     */
    private String name;
    /**
     * entry
     */
    private String entry;
    /**
     * container
     */
    private String container;
    /**
     * activeRule
     */
    private String activeRule;

    public QiankunSlave(Slave slave) {
        this.activeRule = slave.getActiveRule();
        this.container = slave.getContainer();
        this.entry = slave.getEntry();
        this.name = slave.getName();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getActiveRule() {
        return activeRule;
    }

    public void setActiveRule(String activeRule) {
        this.activeRule = activeRule;
    }


}
