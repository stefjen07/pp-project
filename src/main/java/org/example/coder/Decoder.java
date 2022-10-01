package org.example.coder;

public interface Decoder {
    KeyedDecodingContainer container();
    UnkeyedDecodingContainer unkeyedContainer();
    SingleValueDecodingContainer singleValueContainer();

    public Object decode(Class<?> type);
}
