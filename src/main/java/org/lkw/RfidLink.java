package org.lkw;

public class RfidLink {
    private String id;
    private int value;
    private String name;
    private String other;

    public RfidLink() {
    }

    public RfidLink(String id, int value, String name, String other) {
        this.id = id;
        this.value = value;
        this.name = name;
        this.other = other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "RfidLink{" +
                "id='" + id + '\'' +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
