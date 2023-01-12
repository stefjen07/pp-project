package com.stefjen07.webservice;

import com.stefjen07.webservice.dto.ArithmeticRequest;
import com.stefjen07.webservice.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.dto.ArithmeticResponse;
import com.stefjen07.zip.ZipUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {
    final ZipUtil zipUtil;

    final ArithmeticService arithmeticService;

    @PostMapping(path = "/calculate", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<String> calculate(@RequestPart ArithmeticRequestParameters parameters, @RequestPart MultipartFile request) throws IOException {
        String inputContent;

        if(parameters.getInputArchivation()) {
            inputContent = zipUtil.unzip(request.getBytes());
        } else {
            inputContent = new String(request.getBytes());
        }

        ArithmeticRequest parsedRequest = (ArithmeticRequest) arithmeticService
                .createFactoryFor(parameters.getInputType())
                .createDecoder(inputContent)
                .decode(ArithmeticRequest.class);

        ArithmeticResponse response = new ArithmeticResponse();
        response.setResults(
            arithmeticService.calculateArithmeticExpressions(parsedRequest.getExpressions())
        );

        String responseContent = arithmeticService
                .createFactoryFor(parameters.getOutputType())
                .createEncoder()
                .encode(response);

        if(parameters.getOutputArchivation()) {
            responseContent = zipUtil.zip(responseContent, "results." + parameters.getOutputType());
        }

        return ResponseEntity.ok(responseContent);
    }
}
