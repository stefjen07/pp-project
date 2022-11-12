package com.stefjen07.decoder;

public interface SingleValueDecodingContainer {
    String[] getCodingPath();

    Object decode(Class<?> type);
}
