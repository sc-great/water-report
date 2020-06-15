package com.boot.web.controller.admin.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 图标相关
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/demo/icon")
public class DemoIconController
{
    private String prefix = "admin/demo/icon";

    /**
     * FontAwesome图标
     */
    @GetMapping("/fontawesome")
    public String fontAwesome()
    {
        return prefix + "/fontawesome";
    }

    /**
     * Glyphicons图标
     */
    @GetMapping("/glyphicons")
    public String glyphicons()
    {
        return prefix + "/glyphicons";
    }
}
