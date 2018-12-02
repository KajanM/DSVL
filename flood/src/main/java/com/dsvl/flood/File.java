package com.dsvl.flood;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return fileSize == file.fileSize &&
                fileName.equals(file.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileSize);
    }
}
