package com.xuan.dtrun.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Integer id;
    private String account;
    private String password;
    private String username;
    private String iconUrl;
    private Date createTime;
    private int isUse;
    private Integer registerCode;

    public User() {
    }

    public User(Integer id, String account, String password, String username, String iconUrl, Date createTime, int isUse, Integer registerCode) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.username = username;
        this.iconUrl = iconUrl;
        this.createTime = createTime;
        this.isUse = isUse;
        this.registerCode = registerCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public Integer getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(Integer registerCode) {
        this.registerCode = registerCode;
    }
}
