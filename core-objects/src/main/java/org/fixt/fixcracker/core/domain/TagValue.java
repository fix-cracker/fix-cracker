package org.fixt.fixcracker.core.domain;

public class TagValue {
    private final int tag;
    private final String value;

    public TagValue(int tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public int getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TagValue{" +
                "tag=" + tag +
                ", value='" + value + '\'' +
                '}';
    }
}
