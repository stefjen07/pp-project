package com.stefjen07.webservice.dto;

import com.stefjen07.webservice.model.EncodingType;
import lombok.Getter;

@Getter
public class ArithmeticRequestParameters {
    EncodingType inputType;
    Boolean inputEncryption;
    Boolean inputArchivation;
    EncodingType outputType;
    Boolean outputEncryption;
    Boolean outputArchivation;
}
