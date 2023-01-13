package com.stefjen07.webservice.arithmetic.dto;

import com.stefjen07.webservice.arithmetic.model.ArithmeticResult;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ArithmeticResponse {
    List<ArithmeticResult> results;
}
