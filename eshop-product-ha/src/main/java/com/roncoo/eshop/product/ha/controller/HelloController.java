package com.roncoo.eshop.product.ha.controller;

import com.roncoo.eshop.product.ha.annotation.ResponseResult;
import com.roncoo.eshop.product.ha.resp.Result;
import com.roncoo.eshop.product.ha.resp.ResultCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseResult
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(String name) {
        return "hello, " + name;
    }

    @RequestMapping("/age")
    @ResponseResult
    @ResponseBody
    public Result getProduct(Integer id) {
        if (null == id) {
            return Result.failure(id);
        }
        return Result.success(id);
    }
}
