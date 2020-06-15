package com.boot.web.controller.app;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 登录验证拦截器
 *
 *
 */
@Component
public class APPLoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object adminInfo = request.getSession().getAttribute("loginUserInfo");
       // System.out.println("afterCompletion----" + adminInfo + " ::: " + request.getRequestURL());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object adminInfo = request.getSession().getAttribute("loginUserInfo");
      //  System.out.println("postHandle----" + adminInfo + " ::: " + request.getRequestURL());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object adminInfo = request.getSession().getAttribute("loginUserInfo");
        if (adminInfo == null) {
            /*request.setAttribute("msg", "无权限请先登录");*/
            // 获取request返回页面到登录页
            request.getRequestDispatcher("/appSignout").forward(request, response);
            return false;
        }
        return true;
    }
}
