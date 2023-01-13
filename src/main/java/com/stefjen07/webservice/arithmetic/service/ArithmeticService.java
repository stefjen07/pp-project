package com.stefjen07.webservice.arithmetic.service;

import com.stefjen07.factory.FormatFactory;
import com.stefjen07.webservice.arithmetic.model.ArithmeticExpression;
import com.stefjen07.webservice.arithmetic.model.ArithmeticResult;
import com.stefjen07.webservice.arithmetic.model.EncodingType;

import java.util.List;

public interface ArithmeticService {
    FormatFactory createFactoryFor(EncodingType type);

    List<ArithmeticResult> calculateArithmeticExpressions(ArithmeticExpression[] expressions);
}
