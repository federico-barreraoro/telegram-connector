package org.mule.extension.mule.telegram.api.model;

public class Entity implements java.io.Serializable {

    private Integer offset;
    private Integer length;
    private String type;

    public Entity(Integer offset, Integer length, String type) {
        this.offset = offset;
        this.length = length;
        this.type = type;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}