package com.xuan.dtrun.common;

public enum DataEnum {
    LOGINERROR("账号或密码错误"),
    LOGINREFUSE("账号已被封禁"),
    LOGINEXPIRE("登录过期，请重新登录"),
    REGISTERSUCCESS("注册成功"),
    REGISTERFAIL("注册失败"),
    REGISTERALREADY("注册失败,该用户名已存在"),
    REGISTERCODEERROR("注册码无效"),
    REGISTERCODEEXPIRE("该注册码已被使用过"),
    CONNECTIOSUCCESS("数据源连接成功"),
    CONNECTIONFAIL("数据源连接失败，请检查您的数据源参数"),
    CREATESUCCESS("创建成功"),
    CREATEFAIL("创建失败"),
    QUERYSUCCESSFUL("查询成功"),
    QUERYFAILE("查询失败"),
    DELETESUCCESS("删除成功"),
    DELETEFAIL("删除失败"),
    MODIFYFAIL("修改失败"),
    MODIFYSUCCESS("修改成功");
    private String mapping;

    DataEnum(String mapping) {
        this.mapping = mapping;
    }


    @Override
    public String toString() {
        return this.mapping;
    }


}
