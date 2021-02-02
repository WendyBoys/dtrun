package com.xuan.dtrun.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Integer id;
    private String account;
    private String password;
    private String userName;
    private String iconUrl;
    private Date createTime;
    private int isUse;
    private String registerCode;
    private Date lastLoginTime;
    private String lastLoginIp;

    public User() {
    }

    public User(Integer id, String account, String password, String userName, String iconUrl, Date createTime, int isUse, String registerCode) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
}
