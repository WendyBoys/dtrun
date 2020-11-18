package com.xuan.dtrun.common;

public enum MessageEnum {
    SUCCESS("Success"),
    FAIL("Fail"),
    LOGINREFUSE("LoginRefuse");

    private String mapping;

    MessageEnum(String mapping) {
        this.mapping = mapping;
    }


    @Override
    public String toString() {
        return this.mapping;
    }


}
