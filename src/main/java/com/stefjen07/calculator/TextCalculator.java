package com.stefjen07.calculator;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;

public interface TextCalculator {
    String calculate(String inputContent, ArithmeticRequestParameters parameters);

    static TextCalculatorBuilder builder() {
        return new TextCalculatorBuilder(new TextCalculatorImpl(new ArithmeticParser()));
    }
}
