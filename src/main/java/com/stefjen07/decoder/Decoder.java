package com.stefjen07.decoder;

public interface Decoder {
    KeyedDecodingContainer container();
    UnkeyedDecodingContainer unkeyedContainer();
    SingleValueDecodingContainer singleValueContainer();

    public Object decode(Class<?> type);
}
