package org.example.coder;

public interface SingleValueDecodingContainer {
    String[] getCodingPath();

    Object decode(Class<?> type);
}
