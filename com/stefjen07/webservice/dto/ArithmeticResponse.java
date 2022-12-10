package com.stefjen07.webservice.dto;

import com.stefjen07.webservice.model.ArithmeticResult;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class ArithmeticResponse {
    List<ArithmeticResult> results;
}
