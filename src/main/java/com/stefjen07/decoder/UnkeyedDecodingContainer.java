package com.stefjen07.decoder;

public interface UnkeyedDecodingContainer {
    String[] getCodingPath();
    int getCount();
    boolean isAtEnd();

    Object decode(Class<?> type);
}
