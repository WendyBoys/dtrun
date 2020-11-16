package com.xuan.dtrun.common;

public class CommonResult {
    private Integer code;
    private MessageEnum message;
    private Object data;

    public CommonResult(Integer code, MessageEnum message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message.toString();
    }

    public void setMessage(MessageEnum message) {
        this.message = message;
    }

    public Object getData() {
        if(data instanceof DataEnum)
        {
            DataEnum dataEnum = (DataEnum) data;
            return dataEnum.toString();
        }
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
