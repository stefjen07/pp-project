package com.stefjen07.webservice.dto;

import com.stefjen07.webservice.model.EncodingType;
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
    EncodingType outputType;
    Boolean outputEncryption;
    Boolean outputArchivation;
}
