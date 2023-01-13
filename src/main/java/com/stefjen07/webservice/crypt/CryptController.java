package com.stefjen07.webservice.crypt;

import com.stefjen07.crypt.CryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/crypt")
public class CryptController {
    final CryptUtil cryptUtil;

    @SneakyThrows
    @PostMapping(path = "/encrypt", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<String> encrypt(@RequestPart MultipartFile request, @RequestPart String password) {
        String inputContent = new String(request.getBytes(), StandardCharsets.ISO_8859_1);

        return ResponseEntity.ok(cryptUtil.encrypt(inputContent, password));
    }

    @SneakyThrows
    @PostMapping(path = "/decrypt", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<String> decrypt(@RequestPart MultipartFile request, @RequestPart String password) {
        String inputContent = new String(request.getBytes(), StandardCharsets.ISO_8859_1);

        return ResponseEntity.ok(cryptUtil.decrypt(inputContent, password));
    }
}
