package com.roncoo.eshop.product.ha.interceptor;

import com.roncoo.eshop.product.ha.annotation.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zhaokai108
 * @version 0.0.1
 * @description: 拦截器
 * @create 2021-03-22 17:23
 **/
@Slf4j
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

    private static final String PERSION_RESULT_ANN = "PERSION_RESULT_ANN";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (o instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) o;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            //判断是否添加注解
            if (clazz.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(PERSION_RESULT_ANN, clazz.getAnnotation(ResponseResult.class));
            } else if (method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(PERSION_RESULT_ANN, method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
