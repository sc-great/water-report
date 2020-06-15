package com.boot.web.controller.admin.tool;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.boot.common.core.controller.BaseController;

/**
 * swagger 接口
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/tool/swagger")
public class SwaggerController extends BaseController
{
    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index()
    {
        return redirect("/admin/swagger-ui.html");
    }
}
