package com.stefjen07.json;

import com.stefjen07.decoder.*;
import com.stefjen07.KeyValue;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class JSONDecoder implements Decoder {
    public static class KeyedContainer implements KeyedDecodingContainer {
        List<String> raws;
        String[] codingPath;
        List<String> allKeys;

        KeyedContainer(String raw, String[] codingPath) {
            this.codingPath = codingPath;
            this.raws = separate(raw);
            this.allKeys = raws.stream().map(rawElement -> getKeyValue(rawElement).key).collect(Collectors.toList());
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
            return allKeys.toArray(new String[0]);
        }

        @Override
        public Object decode(String key, Class<?> type) {
            var decoder = new JSONDecoder(getRaw(key));
            return decoder.decode(type);
        }
    }

    public static class UnkeyedContainer implements UnkeyedDecodingContainer {
        List<String> raws;
        String[] codingPath;
        int currentIndex = 0;

        UnkeyedContainer(String raw, String[] codingPath) {
            this.codingPath = codingPath;

            try {
                this.raws = new ArrayList<>(separate(raw));
            } catch(Exception e) {
                this.raws = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return raws.size();
        }

        @Override
        public String[] getCodingPath() {
            return codingPath;
        }

        @Override
        public boolean isAtEnd() {
            return currentIndex >= raws.size();
        }

        @Override
        public Object decode(Class<?> type) {
            var decoder = new JSONDecoder(raws.get(currentIndex), codingPath);
            currentIndex += 1;
            return decoder.decode(type);
        }
    }

    class SingleValueContainer implements SingleValueDecodingContainer {
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
                    Object result = type.getDeclaredConstructor().newInstance();

                    var container = container();
                    for (Field field : type.getDeclaredFields()) {
                        field.setAccessible(true);
                        field.set(result, container.decode(field.getName(), field.getType()));
                    }

                    return result;
                }

                if(type.isArray()) {
                    List<Object> result = new ArrayList<>();
                    var container = unkeyedContainer();

                    while(!container.isAtEnd()) {
                        result.add(container.decode(type.getComponentType()));
                    }

                    return result.toArray((Object[]) Array.newInstance(type.getComponentType(), 0));
                }

                if(Boolean.class == type) return Boolean.parseBoolean( raw );
                if(Byte.class == type) return Byte.parseByte( raw );
                if(Short.class == type) return Short.parseShort( raw );
                if(Integer.class == type) return Integer.parseInt( raw );
                if(Long.class == type) return Long.parseLong( raw );
                if(Float.class == type) return Float.parseFloat( raw );
                if(Double.class == type) return Double.parseDouble( raw );
                if(String.class == type) {
                    AtomicReference<String> result = new AtomicReference<>(raw);
                    removeFirst(result, 1);
                    removeLast(result, 1);
                    return result.get();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    String raw;
    String[] codingPath;

    JSONDecoder(String raw, String[] codingPath) {
        this.raw = raw;
        this.codingPath = codingPath;
    }

    public JSONDecoder(String raw) { this(raw, new String[0]); }

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

        AtomicReference<String> currentRaw = new AtomicReference<>("");
        AtomicInteger level = new AtomicInteger();

        text.chars().forEach(character -> {
            switch (character) {
                case '{':
                case '[':
                    level.getAndIncrement();
                    if(level.get() == 1) {
                        currentRaw.set("");
                    } else {
                        concat(currentRaw, (char) character);
                    }
                    break;
                case '}':
                case ']':
                    level.getAndDecrement();
                    if(level.get() == 0) {
                        result.add(currentRaw.get().trim());
                        currentRaw.set("");
                    } else {
                        concat(currentRaw, (char) character);
                    }
                    break;
                case ',':
                    if(level.get() == 1) {
                        result.add(currentRaw.get().trim());
                        currentRaw.set("");
                    } else {
                        concat(currentRaw, (char) character);
                    }
                    break;
                default:
                    concat(currentRaw, (char) character);
                    break;
            }
        });

        return result;
    }

    public static KeyValue getKeyValue(String text) throws RuntimeException {
        AtomicReference<String> key = new AtomicReference<>("");
        AtomicReference<String> value = new AtomicReference<>("");

        AtomicBoolean isValueCurrent = new AtomicBoolean(false);
        AtomicBoolean isTrimmingWhitespaces = new AtomicBoolean(true);

        text.chars().forEach(character -> {
            if(isValueCurrent.get()) {
                if(isTrimmingWhitespaces.get()) {
                    if(character != ' ') {
                        isTrimmingWhitespaces.set(false);
                        concat(value, (char) character);
                    }
                } else {
                    concat(value, (char) character);
                }
            } else if(character == ':') {
                isValueCurrent.set(true);
            } else {
                concat(key, (char) character);
            }
        });

        if(key.get().length() < 1) {
            throw new RuntimeException();
        }

        if(key.get().charAt(0) == '"') {
            removeFirst(key, 1);
            removeLast(key, 1);
        }

        return new KeyValue(key.get(), value.get());
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
