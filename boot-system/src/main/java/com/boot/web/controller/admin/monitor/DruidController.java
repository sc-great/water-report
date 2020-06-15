package com.boot.web.controller.admin.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.boot.common.core.controller.BaseController;

/**
 * druid 监控
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/monitor/data")
public class DruidController extends BaseController
{
    private String prefix = "admin/druid";

    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public String index()
    {
        return redirect(prefix + "/index");
    }
}
