package com.stefjen07.calculator.decorator;

import com.stefjen07.calculator.TextCalculator;
import com.stefjen07.crypt.CryptUtil;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;

public class DecryptionDecorator extends TextCalculatorDecorator {
    CryptUtil cryptUtil = new CryptUtil();
    String password;

    public DecryptionDecorator(TextCalculator calculator, String password) {
        super(calculator);

        this.password = password;
    }

    @Override
    public String calculate(String inputContent, ArithmeticRequestParameters parameters) {
        String decryptedContent;

        try {
            decryptedContent = cryptUtil.decrypt(inputContent, password);
        } catch (Exception e) {
            e.printStackTrace();
            decryptedContent = inputContent;
        }

        return super.calculate(decryptedContent, parameters);
    }
}
