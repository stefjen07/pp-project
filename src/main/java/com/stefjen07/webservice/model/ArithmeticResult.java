package com.stefjen07.webservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ArithmeticResult {
    String name;
    double result;
}
