package com.stefjen07.plain;

import com.stefjen07.encoder.Encoder;
import com.stefjen07.encoder.KeyedEncodingContainer;
import com.stefjen07.encoder.SingleValueEncodingContainer;
import com.stefjen07.encoder.UnkeyedEncodingContainer;
import com.stefjen07.webservice.dto.ArithmeticResponse;

import java.util.stream.Collectors;

public class PlainEncoder implements Encoder {
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
        if(object instanceof ArithmeticResponse) {
            var response = (ArithmeticResponse) object;
            return response.getResults().stream()
                    .map((e) -> e.getName() + " " + e.getResult())
                    .collect(Collectors.joining("\n"));
        }

        return null;
    }
}
