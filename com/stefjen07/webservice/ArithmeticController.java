package com.stefjen07.webservice;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.webservice.dto.ArithmeticRequest;
import com.stefjen07.webservice.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.dto.ArithmeticResponse;
import com.stefjen07.webservice.model.ArithmeticResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {
    ArithmeticParser parser = new ArithmeticParser();

    @PostMapping(path = "/calculate", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<ArithmeticResponse> calculate(@RequestPart ArithmeticRequestParameters parameters, @RequestPart MultipartFile request) {
        ArithmeticRequest parsedRequest = new ArithmeticRequest();
        parsedRequest.setEquations(new ArrayList<>()); // TODO: Parse

        ArithmeticResponse response = ArithmeticResponse.builder().results(
                parsedRequest.getEquations().stream().map((e) -> ArithmeticResult.builder().name(e.getName()).result(parser.parse(e.getEquation())).build()).collect(Collectors.toList())
        ).build();

        return ResponseEntity.ok(response);
    }
}
