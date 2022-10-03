package com.stefjen07.xml;

import com.stefjen07.encoder.*;

import java.util.ArrayList;
import java.util.List;

public class XMLEncoder implements Encoder {
    static class KeyedContainer implements KeyedEncodingContainer {
        String[] codingPath;
        List<String> raws;
        int level;

        KeyedContainer(String[] codingPath, int level) {
            this.codingPath = codingPath;
            this.level = level;
            this.raws = new ArrayList<>();
        }

        @Override
        public void encode(String key, Object object) {
            String[] newCodingPath = new String[codingPath.length + 1];
            System.arraycopy(codingPath, 0, newCodingPath, 0, codingPath.length);
            newCodingPath[codingPath.length] = key;

            var encoder = new XMLEncoder(newCodingPath, level + 1);
            var container = encoder.singleValueContainer();
            container.encode(object);

            var keyValue = new KeyValue(key, encoder.getRaw());
            raws.add(keyValue.getXML());
        }

        @Override
        public String getRaw() {
            return String.join("", raws);
        }
    }

    static class UnkeyedContainer implements UnkeyedEncodingContainer {
        String[] codingPath;
        int count;
        List<String> raws;
        int level;

        UnkeyedContainer(String[] codingPath, int level) {
            this.codingPath = codingPath;
            this.count = 0;
            this.raws = new ArrayList<>();
            this.level = level;
        }

        @Override
        public void encode(Object object) {
            var encoder = new XMLEncoder(codingPath, level+1);

            var container = encoder.singleValueContainer();
            container.encode(object);

            raws.add(encoder.getRaw());
            count += 1;
        }

        @Override
        public String getRaw() {
            return String.join("", (String[]) raws.stream().map(raw -> new KeyValue("item", raw).getXML()).toArray());
        }
    }

    static class SingleValueContainer implements SingleValueEncodingContainer {
        String[] codingPath;
        String raw = "";
        int level;

        SingleValueContainer(String[] codingPath, int level) {
            this.codingPath = codingPath;
            this.level = level;
        }

        @Override
        public void encode(Object object) {
            raw = object.toString();
        }

        @Override
        public String getRaw() {
            return raw;
        }
    }

    String[] codingPath;
    int level;
    EncodingContainer container;

    String getRaw() {
        if(container == null)
            return "";
        return container.getRaw();
    }

    XMLEncoder(String[] codingPath, int level) {
        this.codingPath = codingPath;
        this.level = level;
    }

    public XMLEncoder() {
        this.codingPath = new String[0];
        this.level = 0;
    }

    @Override
    public KeyedEncodingContainer container() {
        return new KeyedContainer(codingPath, level);
    }

    @Override
    public UnkeyedEncodingContainer unkeyedContainer() {
        return new UnkeyedContainer(codingPath, level);
    }

    @Override
    public SingleValueEncodingContainer singleValueContainer() {
        return new SingleValueContainer(codingPath, level);
    }

    @Override
    public String encode(Object object) {
        var container = singleValueContainer();
        container.encode(object);
        return container.getRaw();
    }
}
