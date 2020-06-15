package com.boot.web.controller.admin.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.boot.common.core.controller.BaseController;
import com.boot.framework.web.domain.Server;

/**
 * 服务器监控
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/monitor/server")
public class ServerController extends BaseController
{
    private String prefix = "admin/monitor/server";

    @RequiresPermissions("monitor:server:view")
    @GetMapping()
    public String server(ModelMap mmap) throws Exception
    {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        return prefix + "/server";
    }
}
