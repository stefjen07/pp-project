package com.stefjen07.webservice;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.webservice.dto.ArithmeticRequest;
import com.stefjen07.webservice.dto.ArithmeticResponse;
import com.stefjen07.webservice.model.ArithmeticResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {
    ArithmeticParser parser = new ArithmeticParser();

    @PostMapping("/calculate")
    public ResponseEntity<ArithmeticResponse> sendWordLearningProgress(@RequestBody ArithmeticRequest request) {
        ArithmeticResponse response = ArithmeticResponse.builder().results(
                request.getEquations().stream().map((e) -> ArithmeticResult.builder().name(e.getName()).result(parser.parse(e.getEquation())).build()).collect(Collectors.toList())
        ).build();

        return ResponseEntity.ok(response);
    }
}
