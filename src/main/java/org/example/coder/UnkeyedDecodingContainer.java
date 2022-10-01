package org.example.coder;

public interface UnkeyedDecodingContainer {
    String[] getCodingPath();
    int getCount();
    boolean isAtEnd();

    Object decode(Class<?> type);
}
