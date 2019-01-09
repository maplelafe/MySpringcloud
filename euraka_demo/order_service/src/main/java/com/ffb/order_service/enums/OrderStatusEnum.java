package com.ffb.order_service.enums;

public enum OrderStatusEnum {

    NEW(0,"新订单"),
    FINISHED(1,"完成"),
    CANCLE(3,"订单取消"),
    ;
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
