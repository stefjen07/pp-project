package com.stefjen07.calculator.decorator;

import com.stefjen07.calculator.TextCalculator;
import com.stefjen07.crypt.CryptUtil;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;


public class EncryptionDecorator extends TextCalculatorDecorator {
    CryptUtil cryptUtil = new CryptUtil();
    String password;

    public EncryptionDecorator(TextCalculator calculator, String password) {
        super(calculator);

        this.password = password;
    }

    @Override
    public String calculate(String inputContent, ArithmeticRequestParameters parameters) {
        String outputContent = super.calculate(inputContent, parameters);

        try {
            return cryptUtil.encrypt(outputContent, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputContent;
    }
}
