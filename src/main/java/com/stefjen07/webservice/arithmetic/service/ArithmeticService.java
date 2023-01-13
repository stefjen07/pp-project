package com.stefjen07.webservice.arithmetic.service;

import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;

public interface ArithmeticService {
    String calculateArithmeticExpressions(String inputContent, ArithmeticRequestParameters parameters);
}
