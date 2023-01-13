package com.stefjen07.webservice.arithmetic;

import com.stefjen07.crypt.CryptUtil;
import com.stefjen07.webservice.arithmetic.dto.ArithmeticRequestParameters;
import com.stefjen07.webservice.arithmetic.service.ArithmeticService;
import com.stefjen07.zip.ZipUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {
    final ZipUtil zipUtil;
    final CryptUtil cryptUtil;

    final ArithmeticService arithmeticService;

    @SneakyThrows
    @PostMapping(path = "/calculate", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<String> calculate(@RequestPart ArithmeticRequestParameters parameters, @RequestPart MultipartFile request) {
        String inputContent = new String(request.getBytes(), StandardCharsets.ISO_8859_1);
        String responseContent = arithmeticService.calculateArithmeticExpressions(inputContent, parameters);

        return ResponseEntity.ok(responseContent);
    }
}
