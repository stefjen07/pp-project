package com.stefjen07.webservice;

import com.stefjen07.arithmetic.ArithmeticParser;
import com.stefjen07.decoder.Decoder;
import com.stefjen07.encoder.Encoder;
import com.stefjen07.json.JSONDecoder;
import com.stefjen07.json.JSONEncoder;
import com.stefjen07.webservice.dto.ArithmeticRequest;
import com.stefjen07.webservice.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.dto.ArithmeticResponse;
import com.stefjen07.webservice.model.ArithmeticExpression;
import com.stefjen07.webservice.model.ArithmeticResult;
import com.stefjen07.webservice.model.EncodingType;
import com.stefjen07.xml.XMLDecoder;
import com.stefjen07.xml.XMLEncoder;
import com.stefjen07.zip.ZipUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {
    final ArithmeticParser parser;
    final ZipUtil zipUtil;

    @PostMapping(path = "/calculate", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<String> calculate(@RequestPart ArithmeticRequestParameters parameters, @RequestPart MultipartFile request) throws IOException {
        ArithmeticRequest parsedRequest;

        String inputContent;

        if(parameters.getInputArchivation()) {
            inputContent = zipUtil.unzip(request.getBytes());
        } else {
            inputContent = new String(request.getBytes());
        }

        if(parameters.getInputType() == EncodingType.plain) {
            parsedRequest = new ArithmeticRequest();

            List<ArithmeticExpression> expressions = new ArrayList<>();

            final AtomicReference<ArithmeticExpression> currentExpression = new AtomicReference<>(new ArithmeticExpression());
            AtomicBoolean isWritingName = new AtomicBoolean(true);

            inputContent.chars().forEach((c) -> {
                if(c == '\n') {
                    expressions.add(currentExpression.get());
                    currentExpression.set(new ArithmeticExpression());
                    isWritingName.set(true);
                } else if(c == ' ') {
                    isWritingName.set(false);
                }

                ArithmeticExpression expression = currentExpression.get();
                if(isWritingName.get()) {
                    expression.setName(expression.getName() + (char) c);
                } else {
                    expression.setExpression(expression.getExpression() + (char) c);
                }
                currentExpression.set(expression);
            });

            if(!currentExpression.get().getExpression().isEmpty())
                expressions.add(currentExpression.get());

            parsedRequest.setExpressions(expressions.toArray(new ArithmeticExpression[expressions.size()]));
        } else {
            Decoder decoder;
            switch(parameters.getInputType()) {
                case xml:
                    decoder = new XMLDecoder(inputContent);
                    break;
                case json:
                    decoder = new JSONDecoder(inputContent);
                    break;
                default:
                    throw new RuntimeException();
            }

            parsedRequest = (ArithmeticRequest) decoder.decode(ArithmeticRequest.class);
        }

        ArithmeticResponse response = new ArithmeticResponse();
        response.setResults(
            Arrays.stream(parsedRequest.getExpressions()).map(
                (e) -> ArithmeticResult.builder()
                        .name(e.getName())
                        .result(parser.parse(e.getExpression()))
                        .build()
            )
            .collect(Collectors.toList())
        );

        String responseContent;

        if(parameters.getOutputType() == EncodingType.plain) {
            responseContent = response.getResults().stream().map((e) -> e.getName() + " " + e.getResult()).collect(Collectors.joining("\n"));
        } else {
            Encoder encoder;
            switch (parameters.getOutputType()) {
                case xml:
                    encoder = new XMLEncoder();
                    break;
                case json:
                    encoder = new JSONEncoder();
                    break;
                default:
                    throw new RuntimeException();
            }

            responseContent = encoder.encode(response);
        }

        if(parameters.getOutputArchivation()) {
            responseContent = zipUtil.zip(responseContent, "results." + parameters.getOutputType());
        }

        return ResponseEntity.ok(responseContent);
    }
}
