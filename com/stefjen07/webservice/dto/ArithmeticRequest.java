package com.stefjen07.webservice.dto;

import com.stefjen07.webservice.model.ArithmeticEquation;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class ArithmeticRequest {
    List<ArithmeticEquation> equations;
}