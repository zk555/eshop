package com.roncoo.eshop.product.ha.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author zhaokai108
 * @version 0.0.1
 * @description:
 * @create 2021-03-22 17:19
 **/
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ResponseResult {
}
