package com.stefjen07.zip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    void zip(String archiveName, String filename, byte[] data) {
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archiveName));

            ZipEntry entry = new ZipEntry(filename);
            zout.putNextEntry(entry);
            zout.write(data);
            zout.closeEntry();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void zip(String archiveName, String outputFilename, String inputFilename) {
        try {
            FileInputStream fis = new FileInputStream(inputFilename);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            zip(archiveName, outputFilename, buffer);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
