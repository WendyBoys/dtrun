package com.xuan.dtrun.entity;

public class ResultEntity {
    private Integer id;
    private String startTime;
    private String endTime;
    private String result;
    private String timeConsume;
    private Integer fileCount;
    private Integer mid;

    public ResultEntity() {

    }

    public ResultEntity(String startTime, String endTime, String result, String timeConsume, Integer fileCount, Integer mid) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.result = result;
        this.timeConsume = timeConsume;
        this.fileCount = fileCount;
        this.mid = mid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(String timeConsume) {
        this.timeConsume = timeConsume;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }


    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
