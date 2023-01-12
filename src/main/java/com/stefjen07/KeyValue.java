package com.stefjen07;

public class KeyValue {
    public String key, value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getXML() {
        return "<" + key + ">" + value + "</" + key + ">";
    }
    public String getJSON() { return key + ":" + value; }
}
