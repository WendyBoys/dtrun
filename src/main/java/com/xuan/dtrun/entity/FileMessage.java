package com.xuan.dtrun.entity;

public class FileMessage {
    private String fileName;
    private long fileLength;

    public FileMessage(String fileName, long fileLength) {
        this.fileName = fileName;
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
}
