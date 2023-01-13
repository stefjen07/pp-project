package com.stefjen07.zip;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
public class ZipUtil {
    @SneakyThrows
    public String zip(String content, String filename) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zip(byteArrayOutputStream, filename, content.getBytes());

        return byteArrayOutputStream.toString(StandardCharsets.ISO_8859_1);
    }

    public void zip(String archiveName, String filename, byte[] data) throws FileNotFoundException {
        zip(new FileOutputStream(archiveName), filename, data);
    }

    public void zip(OutputStream outputStream, String filename, byte[] data) {
        try {
            ZipOutputStream zout = new ZipOutputStream(outputStream);

            ZipEntry entry = new ZipEntry(filename);
            zout.putNextEntry(entry);
            zout.write(data, 0, data.length);
            zout.closeEntry();
            zout.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String unzip(String content) {
        return unzip(content.getBytes(StandardCharsets.ISO_8859_1));
    }

    public String unzip(byte[] content) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);

        return new String(unzip(byteArrayInputStream));
    }

    public byte[] unzip(String archiveName, String filename) throws FileNotFoundException {
        return unzip(new FileInputStream(archiveName), filename);
    }

    public byte[] unzip(InputStream inputStream, String filename) {
        try {
            ZipArchiveInputStream zin = new ZipArchiveInputStream(inputStream);

            var entry = zin.getNextEntry();

            while(!entry.getName().equals(filename)) {
                entry = zin.getNextEntry();
            }

            return zin.readAllBytes();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    public byte[] unzip(InputStream inputStream) {
        try {
            ZipArchiveInputStream zin = new ZipArchiveInputStream(inputStream);

            var entry = zin.getNextEntry();
            while(entry.getName().startsWith(".")) {
                entry = zin.getNextEntry();
            }

            return zin.readAllBytes();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
