package com.stefjen07.decoder;

public interface KeyedDecodingContainer {
    String[] getCodingPath();
    String[] getAllKeys();

    Object decode(String key, Class<?> type);
}