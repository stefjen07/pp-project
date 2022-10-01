package com.stefjen07.xml;

import com.stefjen07.decoder.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class XMLDecoder implements Decoder {
    public class KeyedContainer implements KeyedDecodingContainer {
        String[] raws;
        String[] codingPath;
        String[] allKeys;

        KeyedContainer(String raw, String[] codingPath) {
            this.codingPath = codingPath;
            this.raws = (String[]) separate(raw).toArray();
            this.allKeys = (String[]) Arrays.stream(raws).map(rawElement -> {
               String key = getKeyValue(rawElement).key;
               return key;
            }).toArray();
        }

        String getRaw(String key) {
            for(String raw: raws) {
                var item = getKeyValue(raw);

                if(Objects.equals(item.key, key)) {
                    return item.value;
                }
            }

            throw new RuntimeException();
        }

        @Override
        public String[] getCodingPath() {
            return codingPath;
        }

        @Override
        public String[] getAllKeys() {
            return allKeys;
        }

        @Override
        public Object decode(String key, Class<?> type) {
            var decoder = new XMLDecoder(getRaw(key));
            return decoder.decode(type);
        }
    }

    public class UnkeyedContainer implements UnkeyedDecodingContainer {
        String[] raws;
        String[] codingPath;
        int currentIndex = 0;

        UnkeyedContainer(String raw, String[] codingPath) {
            this.codingPath = codingPath;
            this.raws = (String[]) separate(raw).stream().map(rawElement -> getKeyValue(rawElement).value).toArray();
        }

        @Override
        public int getCount() {
            return raws.length;
        }

        @Override
        public String[] getCodingPath() {
            return codingPath;
        }

        @Override
        public boolean isAtEnd() {
            return currentIndex >= raws.length;
        }

        @Override
        public Object decode(Class<?> type) {
            var decoder = new XMLDecoder(raws[currentIndex], codingPath);
            currentIndex += 1;
            return decoder.decode(type);
        }
    }

    public class SingleValueContainer implements SingleValueDecodingContainer {
        String raw;
        String[] codingPath;

        SingleValueContainer(String raw, String[] codingPath) {
            this.raw = raw;
            this.codingPath = codingPath;
        }

        @Override
        public String[] getCodingPath() {
            return codingPath;
        }

        @Override
        public Object decode(Class<?> type) {
            try {
                if (type.isAnnotationPresent(Decodable.class)) {
                    Object result = type.getConstructor().newInstance();

                    var container = container();
                    for (Field field : type.getDeclaredFields()) {
                        field.setAccessible(true);
                        field.set(result, container.decode(field.getName(), field.getType()));
                    }

                    return result;
                }

                if (type.isArray()) {
                    List<Object> result = new ArrayList<>();
                    var container = unkeyedContainer();

                    while(!container.isAtEnd()) {
                        result.add(container.decode(type.componentType()));
                    }

                    return result;
                }

                if (type.isPrimitive()) {
                    if( Boolean.class == type ) return Boolean.parseBoolean( raw );
                    if( Byte.class == type ) return Byte.parseByte( raw );
                    if( Short.class == type ) return Short.parseShort( raw );
                    if( Integer.class == type ) return Integer.parseInt( raw );
                    if( Long.class == type ) return Long.parseLong( raw );
                    if( Float.class == type ) return Float.parseFloat( raw );
                    if( Double.class == type ) return Double.parseDouble( raw );
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    String raw;
    String[] codingPath;

    XMLDecoder(String raw, String[] codingPath) {
        this.raw = raw;
        this.codingPath = codingPath;
    }

    XMLDecoder(String raw) { this(raw, new String[0]); }

    public static KeyValue getKeyValue(String text) {
        AtomicReference<String> key = new AtomicReference<>("");
        AtomicReference<String> value = new AtomicReference<>("");

        text.chars().forEach(character -> {
            if(getLastChar(key.get()) == '>') {
                concat(value, (char) character);
            } else if(character == '<' && key.get().isEmpty()) {
                concat(key, '<');
            } else if(!key.get().isEmpty()) {
                concat(key, (char) character);
            }
        });

        if(key.get().length() <= 2) {
            throw new RuntimeException();
        }

        removeFirst(key, 1);
        removeLast(key, 1);

        return new KeyValue(key.get(), value.get());
    }

    public static char getLastChar(String text) {
        return text.charAt(text.length() - 1);
    }

    public static void concat(AtomicReference<String> text, char character) {
        text.set(text.get() + character);
    }

    public static void removeFirst(AtomicReference<String> text, int k) {
        text.set(text.get().substring(k));
    }

    public static void removeLast(AtomicReference<String> text, int k) {
        int firstIndexToRemove = text.get().length() - k;
        text.set(text.get().substring(0, firstIndexToRemove));
    }

    public static List<String> separate(String text) {
        ArrayList<String> result = new ArrayList<>();

        ArrayDeque<String> tagDeque = new ArrayDeque<>();
        AtomicReference<String> currentTag = new AtomicReference<>("");
        AtomicReference<String> currentRaw = new AtomicReference<>("");

        text.chars().forEach(character -> {
            currentRaw.set(currentRaw.get() + String.valueOf((char) character));

            switch(character) {
                case '<':
                    currentTag.set("<");
                case '>':
                    if(currentTag.get().charAt(1) == '/' && !tagDeque.isEmpty()) {
                        tagDeque.pop();

                        if(!tagDeque.isEmpty()) {
                            currentTag.set(tagDeque.getLast());
                        } else {
                            removeLast(currentRaw, currentTag.get().length() + 1);
                            result.add(currentRaw.get());

                            currentRaw.set("");
                            currentTag.set("");
                        }
                    } else {
                        currentTag.set(currentTag.get() + ">");
                        tagDeque.add(currentTag.get());
                    }
                default:
                    if(currentTag.get().charAt(currentTag.get().length() - 1) != '>' && currentTag.get().charAt(0) == '<') {
                        currentTag.set(currentTag.get() + String.valueOf(character));
                    }
            }
        });

        return result;
    }

    @Override
    public KeyedDecodingContainer container() {
        return new KeyedContainer(raw, codingPath);
    }

    @Override
    public UnkeyedDecodingContainer unkeyedContainer() {
        return new UnkeyedContainer(raw, codingPath);
    }

    @Override
    public SingleValueDecodingContainer singleValueContainer() {
        return new SingleValueContainer(raw, codingPath);
    }

    @Override
    public Object decode(Class<?> type) {
        var container = singleValueContainer();

        return container.decode(type);
    }
}
