package com.stefjen07.calculator.decorator;

import com.stefjen07.calculator.TextCalculator;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;

public class TextCalculatorDecorator implements TextCalculator {
    TextCalculator calculator;

    TextCalculatorDecorator(TextCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public String calculate(String inputContent, ArithmeticRequestParameters parameters) {
        return calculator.calculate(inputContent, parameters);
    }
}
