package com.stefjen07.webservice;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.decoder.Decoder;
import com.stefjen07.json.JSONDecoder;
import com.stefjen07.webservice.dto.ArithmeticRequest;
import com.stefjen07.webservice.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.dto.ArithmeticResponse;
import com.stefjen07.webservice.model.ArithmeticEquation;
import com.stefjen07.webservice.model.ArithmeticResult;
import com.stefjen07.webservice.model.EncodingType;
import com.stefjen07.xml.XMLDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {
    ArithmeticParser parser = new ArithmeticParser();

    @PostMapping(path = "/calculate", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<ArithmeticResponse> calculate(@RequestPart ArithmeticRequestParameters parameters, @RequestPart MultipartFile request) throws IOException {
        ArithmeticRequest parsedRequest;

        StringBuilder inputContentBuilder = new StringBuilder();
        Scanner scanner = new Scanner(request.getInputStream());

        while(scanner.hasNext()) {
            inputContentBuilder.append(scanner.next());
        }

        if(parameters.getInputType() == EncodingType.plain) {
            parsedRequest = new ArithmeticRequest();

            List<ArithmeticEquation> equations = new ArrayList<>();

            final AtomicReference<ArithmeticEquation> currentEquation = new AtomicReference<>(new ArithmeticEquation());
            inputContentBuilder.toString().chars().forEach((c) -> {
                if(c == '\n') {
                    equations.add(currentEquation.get());
                    currentEquation.set(new ArithmeticEquation());
                }

                ArithmeticEquation equation = currentEquation.get();
                equation.setEquation(equation.getEquation() + c);
                currentEquation.set(equation);
            });

            if(!currentEquation.get().getEquation().isEmpty())
                equations.add(currentEquation.get());

            parsedRequest.setEquations(equations.toArray(new ArithmeticEquation[0]));
        } else {
            Decoder decoder;
            switch(parameters.getInputType()) {
                case xml:
                    decoder = new XMLDecoder(inputContentBuilder.toString());
                    break;
                case json:
                    decoder = new JSONDecoder(inputContentBuilder.toString());
                    break;
                default:
                    throw new RuntimeException();
            }

            parsedRequest = (ArithmeticRequest) decoder.decode(ArithmeticRequest.class);
        }

        ArithmeticResponse response = ArithmeticResponse.builder().results(
            Stream.of(parsedRequest.getEquations()).map(
                (e) -> ArithmeticResult.builder()
                        .name(e.getName())
                        .result(parser.parse(e.getEquation()))
                        .build()
            )
            .collect(Collectors.toList())
        ).build();

        return ResponseEntity.ok(response);
    }
}
