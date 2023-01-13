package com.stefjen07.calculator.decorator;

import com.stefjen07.calculator.TextCalculator;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;
import com.stefjen07.zip.ZipUtil;

import java.nio.charset.StandardCharsets;

public class UnzippingDecorator extends TextCalculatorDecorator {
    ZipUtil zipUtil = new ZipUtil();
    String extension;

    public UnzippingDecorator(TextCalculator calculator, String extension) {
        super(calculator);
        this.extension = extension;
    }

    @Override
    public String calculate(String inputContent, ArithmeticRequestParameters parameters) {
        String unzippedContent = zipUtil.unzip(inputContent.getBytes(StandardCharsets.ISO_8859_1), extension);
        return super.calculate(unzippedContent, parameters);
    }
}
