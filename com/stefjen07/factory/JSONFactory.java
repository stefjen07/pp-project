package com.stefjen07.factory;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.encoder.Encoder;
import com.stefjen07.json.JSONDecoder;
import com.stefjen07.json.JSONEncoder;

public class JSONFactory implements FormatFactory {
    @Override
    public Encoder createEncoder() {
        return new JSONEncoder();
    }

    @Override
    public Decoder createDecoder(String content) {
        return new JSONDecoder(content);
    }
}
