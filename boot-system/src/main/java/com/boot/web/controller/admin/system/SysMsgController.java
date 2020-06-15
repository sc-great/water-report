package com.boot.web.controller.admin.system;

import com.boot.common.annotation.Log;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.DateUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysMsg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysMsgService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户消息Controller
 *
 * @author boot
 * @date 2019-12-27
 */
@Api(value = "/admin/system/msg", description = "用户消息")
@Controller
@RequestMapping("/admin/system/msg")
public class SysMsgController extends BaseController {
    private String prefix = "admin/system/msg";

    @Autowired
    private ISysMsgService sysMsgService;

    @RequiresPermissions("sysMsg:sysMsg:list")
    @RequestMapping("/list")
    public String list() {
        return prefix + "/list";
    }

    /**
     * 查询用户消息列表
     */
    @RequiresPermissions("sysMsg:sysMsg:list")
    @RequestMapping("/doList")
    @ResponseBody
    public TableDataInfo doList(SysMsg sysMsg) {
        startPage();
        SysUser sysUser= ShiroUtils.getSysUser();
        sysMsg.setReceiveUserCode(sysUser.getUserCode());
        List<SysMsg> list = sysMsgService.getList(sysMsg);
        return getDataTable(list);
    }

    /**
     * 修改用户消息
     */
    @GetMapping("/info/{id}")
    public String info(@PathVariable("id") String id, ModelMap mmap) {
        SysMsg sysMsg = sysMsgService.getEntityById(id);
        if(sysMsg!=null){
            sysMsg.setMsgState("1");
            sysMsg.setReadTime(DateUtils.getTime());
            sysMsgService.update(sysMsg);
        }
        mmap.put("sysMsg", sysMsg);

        return prefix + "/msgInfo";
    }

    /**
     * 删除用户消息
     */
    @RequiresPermissions("sysMsg:sysMsg:delete")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult doDelete(String ids) {
        return toAjax(sysMsgService.deleteByIds(ids));
    }

    /**
     * 标记已读信息
     */
    @RequiresPermissions("sysMsg:sysMsg:updateRead")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @PostMapping("/updateRead")
    @ResponseBody
    public AjaxResult updateRead(String ids) {
        return toAjax(sysMsgService.updateRead(ids));
    }

    /**
     * 获取数量
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public int getCount(SysMsg sysMsg) {
        SysUser sysUser= ShiroUtils.getSysUser();
        sysMsg.setReceiveUserCode(sysUser.getUserCode());
        return sysMsgService.getCount(sysMsg);
    }
}