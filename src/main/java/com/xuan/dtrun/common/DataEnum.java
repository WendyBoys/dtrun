package com.xuan.dtrun.common;

public enum DataEnum {
    LOGINERROR("账号或密码错误"),
    LOGINREFUSE("账号已被封禁"),
    REGISTERSUCCESS("注册成功"),
    CONNECTIOSUCCESS("数据源连接成功"),
    CONNECTIONFAIL("数据源连接失败，请检查您的数据源参数"),
    CREATESUCCESS("创建成功"),
    CREATEFAIL("创建失败");;

    private String mapping;

    DataEnum(String mapping) {
        this.mapping = mapping;
    }


    @Override
    public String toString() {
        return this.mapping;
    }


}
