package com.stefjen07.webservice.model;

import com.stefjen07.decoder.Decodable;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Decodable
public class ArithmeticEquation {
    String name;
    String equation;
}
