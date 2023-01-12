package com.stefjen07.webservice.dto;

import com.stefjen07.decoder.Decodable;
import com.stefjen07.webservice.model.ArithmeticExpression;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Decodable
public class ArithmeticRequest {
    ArithmeticExpression[] expressions;
}