package com.stefjen07.webservice.arithmetic.dto;

import com.stefjen07.webservice.arithmetic.model.EncodingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArithmeticRequestParameters {
    EncodingType inputType;
    Boolean inputEncryption;
    Boolean inputArchivation;
    String inputPassword;
    EncodingType outputType;
    Boolean outputEncryption;
    Boolean outputArchivation;
    String outputPassword;
}
