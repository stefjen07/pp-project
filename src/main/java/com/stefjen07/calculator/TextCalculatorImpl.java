package com.stefjen07.calculator;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.factory.FormatFactory;
import com.stefjen07.factory.JSONFactory;
import com.stefjen07.factory.PlainFactory;
import com.stefjen07.factory.XMLFactory;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequest;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticResponse;
import com.stefjen07.webservice.arithmetic.model.ArithmeticExpression;
import com.stefjen07.webservice.arithmetic.model.ArithmeticResult;
import com.stefjen07.webservice.arithmetic.model.EncodingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TextCalculatorImpl implements TextCalculator {
    final ArithmeticParser parser;

    public FormatFactory createFactoryFor(EncodingType type) {
        switch(type) {
            case txt:
                return new PlainFactory();
            case xml:
                return new XMLFactory();
            case json:
                return new JSONFactory();
        }

        return null;
    }

    public List<ArithmeticResult> calculateArithmeticExpressions(ArithmeticExpression[] expressions) {
        return Arrays.stream(expressions).map(
                        (e) -> ArithmeticResult.builder()
                                .name(e.getName())
                                .result(parser.parse(e.getExpression()))
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public String calculate(String inputContent, ArithmeticRequestParameters parameters) {
        ArithmeticRequest parsedRequest = (ArithmeticRequest)
                createFactoryFor(parameters.getInputType())
                .createDecoder(inputContent)
                .decode(ArithmeticRequest.class);

        ArithmeticResponse response = new ArithmeticResponse();
        response.setResults(
                calculateArithmeticExpressions(parsedRequest.getExpressions())
        );

        return createFactoryFor(parameters.getOutputType())
                .createEncoder()
                .encode(response);
    }
}
