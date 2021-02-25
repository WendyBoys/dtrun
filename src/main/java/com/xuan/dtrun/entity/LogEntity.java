package com.xuan.dtrun.entity;

public class LogEntity {
    private Integer id;
    private String log;
    private String createTime;
    private String color;
    private Integer uid;

    public LogEntity() {
    }

    public LogEntity(Integer id, String log, String createTime, String color, Integer uid) {
        this.id = id;
        this.log = log;
        this.createTime = createTime;
        this.color = color;
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
