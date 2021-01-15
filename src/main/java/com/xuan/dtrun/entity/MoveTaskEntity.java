package com.xuan.dtrun.entity;

import java.io.Serializable;

public class MoveTaskEntity implements Serializable {
    private Integer id;
    private String taskName;
    private String taskJson;
    private String status;
    private String result;
    private String createTime;
    private String finishTime;
    private Integer uid;
    private Integer key;

    public MoveTaskEntity() {
    }

    public MoveTaskEntity(Integer id,String taskName, String taskJson) {
        this.id=id;
        this.taskName = taskName;
        this.taskJson = taskJson;
    }

    public MoveTaskEntity(String taskName, String taskJson, String status, String createTime, Integer uid) {
        this.taskName = taskName;
        this.taskJson = taskJson;
        this.status = status;
        this.createTime = createTime;
        this.uid = uid;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskJson() {
        return taskJson;
    }

    public void setTaskJson(String taskJson) {
        this.taskJson = taskJson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
