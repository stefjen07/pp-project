package com.stefjen07.xml;

public class KeyValue {
    String key, value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getXML() {
        return "<" + key + ">" + value + "</" + key + ">";
    }
}
