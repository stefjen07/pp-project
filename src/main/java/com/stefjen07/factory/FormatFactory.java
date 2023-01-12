package com.stefjen07.factory;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.encoder.Encoder;

public interface FormatFactory {
    Encoder createEncoder();
    Decoder createDecoder(String content);
}
