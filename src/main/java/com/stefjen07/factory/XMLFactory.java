package com.stefjen07.factory;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.encoder.Encoder;
import com.stefjen07.xml.XMLDecoder;
import com.stefjen07.xml.XMLEncoder;

public class XMLFactory implements FormatFactory {
    @Override
    public Encoder createEncoder() {
        return new XMLEncoder();
    }

    @Override
    public Decoder createDecoder(String content) {
        return new XMLDecoder(content);
    }
}
