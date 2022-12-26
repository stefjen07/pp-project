package com.stefjen07.webservice.model;

import com.stefjen07.decoder.Decodable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@Decodable
public class ArithmeticExpression {
    String name = "";
    String expression = "";
}
