package com.stefjen07.encoder;

public interface Encoder {
    KeyedEncodingContainer container();
    UnkeyedEncodingContainer unkeyedContainer();
    SingleValueEncodingContainer singleValueContainer();

    String encode(Object object);
}
