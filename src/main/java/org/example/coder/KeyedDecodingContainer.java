package org.example.coder;

public interface KeyedDecodingContainer {
    String[] getCodingPath();
    String[] getAllKeys();

    Object decode(String key, Class<?> type);
}