package com.stefjen07.json;

import com.stefjen07.encoder.Encoder;
import com.stefjen07.encoder.KeyedEncodingContainer;
import com.stefjen07.encoder.SingleValueEncodingContainer;
import com.stefjen07.encoder.UnkeyedEncodingContainer;

public class JSONEncoder implements Encoder {
    @Override
    public KeyedEncodingContainer container() {
        return null;
    }

    @Override
    public UnkeyedEncodingContainer unkeyedContainer() {
        return null;
    }

    @Override
    public SingleValueEncodingContainer singleValueContainer() {
        return null;
    }

    @Override
    public String encode(Object object) {
        return null;
    }
}
