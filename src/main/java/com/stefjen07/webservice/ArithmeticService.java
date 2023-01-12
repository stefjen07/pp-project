package com.stefjen07.webservice;

import com.stefjen07.factory.FormatFactory;
import com.stefjen07.webservice.model.ArithmeticExpression;
import com.stefjen07.webservice.model.ArithmeticResult;
import com.stefjen07.webservice.model.EncodingType;

import java.util.List;

public interface ArithmeticService {
    FormatFactory createFactoryFor(EncodingType type);

    List<ArithmeticResult> calculateArithmeticExpressions(ArithmeticExpression[] expressions);
}
