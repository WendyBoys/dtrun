package com.xuan.dtrun.common;

public enum DataEnum {
    LOGINERROR("账号或密码错误"),
    LOGINREFUSE("账号已被封禁"),
    REGISTERSUCCESS("注册成功");

    private String mapping;

    DataEnum(String mapping) {
        this.mapping = mapping;
    }


    @Override
    public String toString() {
        return this.mapping;
    }


}
