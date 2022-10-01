package com.stefjen07.encoder;

public interface Encoder {
    KeyedEncodingContainer container();
    UnkeyedEncodingContainer unkeyedContainer();
    SingleValueEncodingContainer singleValueContainer();

    public String encode(Object object);
}
