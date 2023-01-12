package com.stefjen07.factory;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.encoder.Encoder;
import com.stefjen07.plain.PlainDecoder;
import com.stefjen07.plain.PlainEncoder;

public class PlainFactory implements FormatFactory {
    @Override
    public Encoder createEncoder() {
        return new PlainEncoder();
    }

    @Override
    public Decoder createDecoder(String content) {
        return new PlainDecoder(content);
    }
}
