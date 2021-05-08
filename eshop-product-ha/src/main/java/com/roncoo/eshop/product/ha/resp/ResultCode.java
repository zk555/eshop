package com.roncoo.eshop.product.ha.resp;

/**
 * @author zhaokai108
 * @version 0.0.1
 * @description: 返回状态码
 * @create 2021-03-22 16:06
 **/
public enum ResultCode {
    SUCCESS(1, "成功"),
    ERROR(500, "失败");

    /**
     * 返回码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
