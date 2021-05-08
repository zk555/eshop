package com.roncoo.eshop.product.ha.resp;

import lombok.Data;

/**
 * @author zhaokai108
 * @version 0.0.1
 * @description:
 * @create 2021-03-22 17:53
 **/
@Data
public class ErrorResult {

    private Integer code;

    private String message;

    private Object errors;

    public ErrorResult(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.errors = data;
    }


}
