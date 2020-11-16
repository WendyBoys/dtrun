package com.xuan.dtrun.common;

public enum MessageEnum {
    SUCCESS("操作成功"),
    FAIL("操作失败"),
    LOGINREFUSE("账号已被封禁");

    private String mapping;

    MessageEnum(String mapping) {
        this.mapping=mapping;
    }


    @Override
    public String toString() {
        return this.mapping;
    }




}
