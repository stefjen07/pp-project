package com.stefjen07.json;

import com.stefjen07.KeyValue;
import com.stefjen07.encoder.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JSONEncoder implements Encoder {
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

            var encoder = new JSONEncoder(newCodingPath, level + 1);
            var container = encoder.singleValueContainer();
            container.encode(object);

            var keyValue = new KeyValue(key, encoder.getRaw());
            raws.add(keyValue.getJSON());
        }

        @Override
        public String getRaw() {
            return "{" + String.join(",", raws) + "}";
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
            var encoder = new JSONEncoder(codingPath, level+1);

            var container = encoder.singleValueContainer();
            container.encode(object);

            raws.add(encoder.getRaw());
            count += 1;
        }

        @Override
        public String getRaw() {
            return "[" + String.join(",", raws) + "]";
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
            raw = new JSONEncoder().encode(object);
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

    JSONEncoder(String[] codingPath, int level) {
        this.codingPath = codingPath;
        this.level = level;
    }

    public JSONEncoder() {
        this.codingPath = new String[0];
        this.level = 0;
    }

    @Override
    public KeyedEncodingContainer container() {
        KeyedEncodingContainer container = new KeyedContainer(codingPath, level);
        this.container = container;
        return container;
    }

    @Override
    public UnkeyedEncodingContainer unkeyedContainer() {
        UnkeyedEncodingContainer container = new UnkeyedContainer(codingPath, level);
        this.container = container;
        return container;
    }

    @Override
    public SingleValueEncodingContainer singleValueContainer() {
        SingleValueEncodingContainer container = new SingleValueContainer(codingPath, level);
        this.container = container;
        return container;
    }

    @Override
    public String encode(Object object) {
        var type = object.getClass();

        if(Collection.class.isAssignableFrom(type) || type.isArray()) {
            var container = unkeyedContainer();

            for(var element: (Collection<Object>) object) {
                container.encode(element);
            }

            return container.getRaw();
        } else if( Boolean.class == type || Byte.class == type || Short.class == type || Integer.class == type || Long.class == type || Float.class == type || Double.class == type || String.class == type) {
            String result =  object.toString();
            if(String.class == type) {
                result = "\"" + result + "\"";
            }
            return result;
        } else {
            var container = container();

            try {
                for (Field field : type.getDeclaredFields()) {
                    if(!field.canAccess(object))
                        field.setAccessible(true);

                    container.encode(field.getName(), field.get(object));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            return container.getRaw();
        }
    }
}
