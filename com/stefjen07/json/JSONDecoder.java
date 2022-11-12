package com.stefjen07.json;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.decoder.KeyedDecodingContainer;
import com.stefjen07.decoder.SingleValueDecodingContainer;
import com.stefjen07.decoder.UnkeyedDecodingContainer;

public class JSONDecoder implements Decoder {

    @Override
    public KeyedDecodingContainer container() {
        return null;
    }

    @Override
    public UnkeyedDecodingContainer unkeyedContainer() {
        return null;
    }

    @Override
    public SingleValueDecodingContainer singleValueContainer() {
        return null;
    }

    @Override
    public Object decode(Class<?> type) {
        return null;
    }
}
