package com.justdoit.keller;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.ErrorLogUtils;
import com.justdoit.keller.common.util.RequestUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * @author yangkaile
 * @date 2020-05-08 14:49:09
 */
@Component
public class MyInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Console.println("preHandle",RequestUtil.getRealIp(request),PublicConstant.address);
        boolean check = PublicConstant.address.equals(RequestUtil.getRealIp(request));
        if(!check){
            ErrorLogUtils.accessErrorLog(request);
        }
        return check;
    }


    public MyInterceptor() {
        super();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
