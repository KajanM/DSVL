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
        if (!(o instanceof File)) return false;
        File file = (File) o;
        return getFileName().equals(file.getFileName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileName());
    }

}
