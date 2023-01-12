package com.stefjen07.plain;

import com.stefjen07.decoder.Decoder;
import com.stefjen07.decoder.KeyedDecodingContainer;
import com.stefjen07.decoder.SingleValueDecodingContainer;
import com.stefjen07.decoder.UnkeyedDecodingContainer;
import com.stefjen07.webservice.dto.ArithmeticRequest;
import com.stefjen07.webservice.model.ArithmeticExpression;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class PlainDecoder implements Decoder {
    String content;


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
        ArithmeticRequest request = new ArithmeticRequest();

        List<ArithmeticExpression> expressions = new ArrayList<>();

        final AtomicReference<ArithmeticExpression> currentExpression = new AtomicReference<>(new ArithmeticExpression());
        AtomicBoolean isWritingName = new AtomicBoolean(true);

        content.chars().forEach((c) -> {
            if(c == '\n') {
                expressions.add(currentExpression.get());
                currentExpression.set(new ArithmeticExpression());
                isWritingName.set(true);
            } else if(c == ' ') {
                isWritingName.set(false);
            }

            ArithmeticExpression expression = currentExpression.get();
            if(isWritingName.get()) {
                expression.setName(expression.getName() + (char) c);
            } else {
                expression.setExpression(expression.getExpression() + (char) c);
            }
            currentExpression.set(expression);
        });

        if(!currentExpression.get().getExpression().isEmpty())
            expressions.add(currentExpression.get());

        request.setExpressions(expressions.toArray(new ArithmeticExpression[expressions.size()]));

        return request;
    }
}
