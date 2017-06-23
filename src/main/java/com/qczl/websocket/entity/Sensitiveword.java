package com.qczl.websocket.entity;

import java.io.Serializable;

public class Sensitiveword implements Serializable{

    private static final long serialVersionUID = -7626218341752861742L;

    /**id*/
    private Integer id;

    /**敏感词*/
    private String keenness;

    /**替换词*/
    private String substitution;

    /**敏感级别 0一般 1敏感*/
    private Integer levels;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeenness() {
        return keenness;
    }

    public void setKeenness(String keenness) {
        this.keenness = keenness;
    }

    public String getSubstitution() {
        return substitution;
    }

    public void setSubstitution(String substitution) {
        this.substitution = substitution;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }
}
