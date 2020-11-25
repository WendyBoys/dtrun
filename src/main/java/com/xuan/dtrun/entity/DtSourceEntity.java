package com.xuan.dtrun.entity;

import java.io.Serializable;
import java.util.Date;

public class DtSourceEntity implements Serializable {
    private int id;
    private String dtsourcename;
    private String dtSourceType;
    private String dtsourceJson;
    private Date createTime;
    private User user;

    public DtSourceEntity() {
    }

    public DtSourceEntity(int id, String dtsourcename, String dtSourceType, String dtsourceJson, Date createTime,User user) {
        this.id = id;
        this.dtsourcename = dtsourcename;
        this.dtSourceType = dtSourceType;
        this.dtsourceJson = dtsourceJson;
        this.createTime = createTime;
        this.user=user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDtsourcename() {
        return dtsourcename;
    }

    public void setDtsourcename(String dtsourcename) {
        this.dtsourcename = dtsourcename;
    }

    public String getDtSourceType() {
        return dtSourceType;
    }

    public void setDtSourceType(String dtSourceType) {
        this.dtSourceType = dtSourceType;
    }

    public String getDtsourceJson() {
        return dtsourceJson;
    }

    public void setDtsourceJson(String dtsourceJson) {
        this.dtsourceJson = dtsourceJson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
