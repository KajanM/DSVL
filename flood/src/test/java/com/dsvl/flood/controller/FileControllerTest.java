package com.dsvl.flood.controller;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class FileControllerTest {

    @Test
    public void createFileOnTheFly() throws IOException {
        String toWrite = "Hello";
        File tmpFile = File.createTempFile("panda", ".txt");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(toWrite);
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        assertEquals(toWrite, reader.readLine());
        reader.close();
    }

}