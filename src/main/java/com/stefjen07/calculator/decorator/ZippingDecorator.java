package com.stefjen07.calculator.decorator;

import com.stefjen07.calculator.TextCalculator;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;
import com.stefjen07.zip.ZipUtil;

public class ZippingDecorator extends TextCalculatorDecorator {
    ZipUtil zipUtil = new ZipUtil();

    public ZippingDecorator(TextCalculator calculator) {
        super(calculator);
    }

    @Override
    public String calculate(String inputContent, ArithmeticRequestParameters parameters) {
        String outputContent = super.calculate(inputContent, parameters);
        return zipUtil.zip(outputContent, "results." + parameters.getOutputType());
    }
}
