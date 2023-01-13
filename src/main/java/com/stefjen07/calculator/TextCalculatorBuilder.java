package com.stefjen07.calculator;

import com.stefjen07.calculator.decorator.DecryptionDecorator;
import com.stefjen07.calculator.decorator.EncryptionDecorator;
import com.stefjen07.calculator.decorator.UnzippingDecorator;
import com.stefjen07.calculator.decorator.ZippingDecorator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextCalculatorBuilder {
    @NonNull
    TextCalculator calculator;

    public void encryption(String password) {
        calculator = new EncryptionDecorator(calculator, password);
    }

    public void decryption(String password) {
        calculator = new DecryptionDecorator(calculator, password);
    }

    public void zipping() {
        calculator = new ZippingDecorator(calculator);
    }

    public void unzipping(String extension) {
        calculator = new UnzippingDecorator(calculator, extension);
    }

    public TextCalculator build() {
        return calculator;
    }
}
