package com.stefjen07.webservice.arithmetic.service;

import com.stefjen07.calculator.TextCalculator;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ArithmeticServiceImpl implements ArithmeticService {
    @Override
    public String calculateArithmeticExpressions(String inputContent, ArithmeticRequestParameters parameters) {
        var builder = TextCalculator.builder();

        if(parameters.getInputEncryption()) {
            builder.decryption(parameters.getInputPassword());
        }

        if(parameters.getInputArchivation()) {
            builder.unzipping(parameters.getInputType().name());
        }

        if(parameters.getOutputArchivation()) {
            builder.zipping();
        }

        if(parameters.getOutputEncryption()) {
            builder.encryption(parameters.getOutputPassword());
        }

        return builder.build().calculate(inputContent, parameters);
    }
}
