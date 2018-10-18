package com.dsvl.flood;

public class File {

    private String fileName;
    private int fileSize;

    public File(String fileName) {
        this.fileName = fileName;
    }

    public File(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void rename(String newName) {
        this.fileName = newName;
    }
}
