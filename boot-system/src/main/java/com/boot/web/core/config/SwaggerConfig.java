package com.boot.web.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.boot.common.config.Global;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的接口配置
 * 
 * @author epl
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
                //.apis(RequestHandlerSelectors.basePackage("com.boot.project.tool.swagger"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题：易普利科技集采管理系统_接口文档")
                // 描述
                .description("描述：用于管理集采管理系统相关功能模块的接口信息")
                // 作者信息
                .contact(new Contact(Global.getName(), null, null))
                // 版本
                .version("版本号:" + Global.getVersion())
                .build();
    }
}