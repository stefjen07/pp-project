package com.stefjen07.webservice;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.factory.FormatFactory;
import com.stefjen07.factory.JSONFactory;
import com.stefjen07.factory.PlainFactory;
import com.stefjen07.factory.XMLFactory;
import com.stefjen07.webservice.model.ArithmeticExpression;
import com.stefjen07.webservice.model.ArithmeticResult;
import com.stefjen07.webservice.model.EncodingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
@Component
public class ArithmeticServiceImpl implements ArithmeticService {
    final ArithmeticParser parser;

    @Override
    public FormatFactory createFactoryFor(EncodingType type) {
        switch(type) {
            case plain:
                return new PlainFactory();
            case xml:
                return new XMLFactory();
            case json:
                return new JSONFactory();
        }

        return null;
    }

    @Override
    public List<ArithmeticResult> calculateArithmeticExpressions(ArithmeticExpression[] expressions) {
        return Arrays.stream(expressions).map(
                        (e) -> ArithmeticResult.builder()
                                .name(e.getName())
                                .result(parser.parse(e.getExpression()))
                                .build()
                )
                .collect(Collectors.toList());
    }
}
