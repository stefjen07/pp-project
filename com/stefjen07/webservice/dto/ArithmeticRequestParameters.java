package com.stefjen07.webservice.dto;

import com.stefjen07.webservice.model.EncodingType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArithmeticRequestParameters {
    EncodingType inputType;
    Boolean inputEncryption;
    Boolean inputArchivation;
    EncodingType outputType;
    Boolean outputEncryption;
    Boolean outputArchivation;
}
