package com.stefjen07;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.arithmetic.model.EncodingType;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTests {
    @LocalServerPort
    final int port = 8080;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void testCalculate() throws JsonProcessingException {
        RequestSpecification request = RestAssured.given();

        var parameters = ArithmeticRequestParameters.builder()
                .inputType(EncodingType.txt)
                .inputEncryption(false)
                .inputArchivation(false)
                .outputType(EncodingType.txt)
                .outputEncryption(false)
                .outputArchivation(false)
                .build();
        request.multiPart(new MultiPartSpecBuilder(new ObjectMapper().writeValueAsString(parameters).getBytes()).
                fileName("parameters.json").
                controlName("parameters").
                mimeType("application/json").
                build());
        request.multiPart(new MultiPartSpecBuilder("hello 1+2+3".getBytes()).
                fileName("request.txt").
                controlName("request").
                mimeType("text/plain").
                build());
        request.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        request.accept("application/json, text/plain, */*");
        Response response = request.post("/arithmetic/calculate");
        Assertions.assertEquals("hello 6.0", response.getBody().print());
    }

    @Test
    public void testZipCalculate() throws IOException {
        RequestSpecification request = RestAssured.given();

        var parameters = ArithmeticRequestParameters.builder()
                .inputType(EncodingType.json)
                .inputEncryption(false)
                .inputArchivation(true)
                .outputType(EncodingType.txt)
                .outputEncryption(false)
                .outputArchivation(false)
                .build();
        request.multiPart(new MultiPartSpecBuilder(new ObjectMapper().writeValueAsString(parameters).getBytes()).
                fileName("parameters.json").
                controlName("parameters").
                mimeType("application/json").
                build());

        request.multiPart(new MultiPartSpecBuilder(getClass().getClassLoader().getResourceAsStream("expressions.json.zip").readAllBytes()).
                fileName("expressions.zip").
                controlName("request").
                mimeType("application/zip").
                build());
        request.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        request.accept("application/json, text/plain, */*");
        Response response = request.post("/arithmetic/calculate");
        Assertions.assertEquals("apples 6.0\ndata 12.0", response.getBody().print());
    }
}