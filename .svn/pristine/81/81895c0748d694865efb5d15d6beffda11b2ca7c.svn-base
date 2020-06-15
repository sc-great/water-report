package com.boot.web.controller.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * 登录拦截器配置
 * */
@Configuration
public class APPMvcConfigurer implements WebMvcConfigurer {
    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/appLogin").setViewName("app/login.html");
        registry.addViewController("/appSignout").setViewName("app/signout.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new APPLoginHandlerInterceptor())
                .addPathPatterns("/app/**")//拦截的地址
                .excludePathPatterns("/app/index/**","/app/login","/app/doLogin","/app/signOut","/app/visitor/**","/app/doVisitor");//放行的地址
    }
}
