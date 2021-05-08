package com.roncoo.eshop.product.ha.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaokai108
 * @version 0.0.1
 * @description:
 * @create 2021-03-22 16:05
 **/
@Data
public class Result implements Serializable {

    private Integer code;

    private String message;

    private Object data;

    public Result(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;

    }

    public Result() {
    }

    //返回成功
    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    //返回成功
    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    //返回失败
    public static Result failure() {
        Result result = new Result();
        result.setResultCode(ResultCode.ERROR);
        return result;
    }

    //返回失败
    public static Result failure(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.ERROR);
        if (data instanceof ErrorResult) {
            ErrorResult errorResult = (ErrorResult) data;
            result.setData(errorResult);
        }
        return result;
    }

    private void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}
