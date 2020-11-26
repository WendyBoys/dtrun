package com.xuan.dtrun.entity;

import java.io.Serializable;


public class DtSourceEntity implements Serializable {
    private int id;
    private String dtSourceName;
    private String dtSourceType;
    private String dtSourceJson;
    private String createTime;
    private User user;
    private int uid;

    public DtSourceEntity() {
    }

    public DtSourceEntity(int id, String dtSourceName, String dtSourceType, String dtSourceJson, String createTime, User user, int uid) {
        this.id = id;
        this.dtSourceName = dtSourceName;
        this.dtSourceType = dtSourceType;
        this.dtSourceJson = dtSourceJson;
        this.createTime = createTime;
        this.user=user;
        this.uid=uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDtSourceName() {
        return dtSourceName;
    }

    public void setDtSourceName(String dtSourceName) {
        this.dtSourceName = dtSourceName;
    }

    public String getDtSourceType() {
        return dtSourceType;
    }

    public void setDtSourceType(String dtSourceType) {
        this.dtSourceType = dtSourceType;
    }

    public String getDtSourceJson() {
        return dtSourceJson;
    }

    public void setDtSourceJson(String dtSourceJson) {
        this.dtSourceJson = dtSourceJson;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
