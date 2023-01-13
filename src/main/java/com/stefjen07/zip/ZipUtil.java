package com.stefjen07.zip;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
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

    public String unzip(String content, String extension) {
        return unzip(content.getBytes(StandardCharsets.ISO_8859_1), extension);
    }

    public String unzip(byte[] content, String extension) {
        return new String(unzip(() -> new ByteArrayInputStream(content), extension));
    }

    public byte[] unzip(String filename, InputStream inputStream) {
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

    public byte[] unzip(Callable<InputStream> inputStream, String extension) {
        ArrayList<ArchiveInputStream> streamsToTry = null;
        try {
            streamsToTry = new ArrayList<>(List.of(new ArchiveInputStream[]{
                    new ZipArchiveInputStream(inputStream.call()),
                    new TarArchiveInputStream(inputStream.call()),
                    new ArArchiveInputStream(inputStream.call()),
                    new CpioArchiveInputStream(inputStream.call()),
                    new JarArchiveInputStream(inputStream.call())
            }));

            streamsToTry.add(new ArjArchiveInputStream(inputStream.call()));
        } catch (Exception ignored) {

        }

        for (var zin: streamsToTry) {
            try {
                var entry = zin.getNextEntry();
                while(!entry.getName().matches("^.+\\.(" + extension + "|zip)$")) {
                    entry = zin.getNextEntry();
                }

                byte[] bytes = zin.readAllBytes();
                if(entry.getName().matches("^.+\\.(tar|ar|arj|jar|cpio|zip|7z)$")) {
                    return unzip(() -> new ByteArrayInputStream(bytes), extension);
                } else {
                    return bytes;
                }
            } catch(Exception ignored) {

            }
        }

        try {
            var sevenZip = new SevenZFile(new SeekableInMemoryByteChannel(inputStream.call().readAllBytes()));

            var entry = sevenZip.getNextEntry();
            while(!entry.getName().matches("^.+\\.(" + extension + "|zip)$")) {
                entry = sevenZip.getNextEntry();
            }

            byte[] bytes = sevenZip.getInputStream(entry).readAllBytes();
            if(entry.getName().matches("^.+\\.(tar|ar|arj|jar|cpio|zip|7z)$")) {
                return unzip(() -> new ByteArrayInputStream(bytes), extension);
            } else {
                return bytes;
            }
        }  catch(Exception ignored) {

        }


        return new byte[0];
    }
}
