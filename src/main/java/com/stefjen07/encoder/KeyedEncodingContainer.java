package com.stefjen07.encoder;

public interface KeyedEncodingContainer extends EncodingContainer {
    void encode(String key, Object object);
}
