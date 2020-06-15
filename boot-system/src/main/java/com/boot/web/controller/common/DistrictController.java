package com.boot.web.controller.common;

import com.boot.common.core.domain.AjaxResult;
import com.boot.system.service.ISysDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 省市县数据Controller
 *
 * @author EPL
 * @date 2019-09-11
 */
@Controller
@RequestMapping("/district")
public class DistrictController {
    @Autowired
    private ISysDistrictService sysDistrictService;

    /**
     * 修改省市县数据
     */
    @GetMapping("/get")
    @ResponseBody
    public AjaxResult selectSysDistrictByPid(String pid) {
        return AjaxResult.success("获取成功",sysDistrictService.selectSysDistrictByPid(pid));
    }
}