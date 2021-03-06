package com.boot.web.controller.admin.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.boot.common.core.controller.BaseController;
import com.boot.common.utils.file.FileUploadUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.boot.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 表单相关
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/demo/form")
public class DemoFormController extends BaseController
{
    private String prefix = "admin/demo/form";

    private final static List<UserFormModel> users = new ArrayList<UserFormModel>();
    {
        users.add(new UserFormModel(1, "1000001", "测试1", "15888888888"));
        users.add(new UserFormModel(2, "1000002", "测试2", "15666666666"));
        users.add(new UserFormModel(3, "1000003", "测试3", "15666666666"));
        users.add(new UserFormModel(4, "1000004", "测试4", "15666666666"));
        users.add(new UserFormModel(5, "1000005", "测试5", "15666666666"));
    }

    /**
     * 按钮页
     */
    @GetMapping("/button")
    public String button()
    {
        return prefix + "/button";
    }

    /**
     * 下拉框
     */
    @GetMapping("/select")
    public String select()
    {
        return prefix + "/select";
    }

    /**
     * 表单校验
     */
    @GetMapping("/validate")
    public String validate()
    {
        return prefix + "/validate";
    }

    /**
     * 功能扩展（包含文件上传）
     */
    @GetMapping("/jasny")
    public String jasny()
    {
        return prefix + "/jasny";
    }

    /**
     * 拖动排序
     */
    @GetMapping("/sortable")
    public String sortable()
    {
        return prefix + "/sortable";
    }

    /**
     * 选项卡 & 面板
     */
    @GetMapping("/tabs_panels")
    public String tabs_panels()
    {
        return prefix + "/tabs_panels";
    }

    /**
     * 栅格
     */
    @GetMapping("/grid")
    public String grid()
    {
        return prefix + "/grid";
    }

    /**
     * 表单向导
     */
    @GetMapping("/wizard")
    public String wizard()
    {
        return prefix + "/wizard";
    }

    /**
     * 文件上传
     */
    @GetMapping("/upload")
    public String upload()
    {
        return prefix + "/upload";
    }
    @ResponseBody
    @RequestMapping("/doUpload")
    public AjaxResult doUpload(@RequestParam("file") MultipartFile file){
        AjaxResult ajaxResult;
        try {
            if (!file.isEmpty()){
                String filePath =FileUploadUtils.upload(file);
                ajaxResult=success("文件上传成功！",filePath);
            }else{
                ajaxResult=error();
            }
        }catch (Exception e){
            ajaxResult=error();
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult doDelete(@RequestParam("path") String path){
        AjaxResult ajaxResult;
        try {
            if(FileUploadUtils.delete(path)){
                ajaxResult=success("文件删除成功！",path);
            }else{
                ajaxResult=error();
            }
        }catch (Exception e){
            ajaxResult=error();
        }
        return ajaxResult;
    }
    /**
     * 日期和时间页
     */
    @GetMapping("/datetime")
    public String datetime()
    {
        return prefix + "/datetime";
    }

    /**
     * 左右互选组件
     */
    @GetMapping("/duallistbox")
    public String duallistbox()
    {
        return prefix + "/duallistbox";
    }

    /**
     * 基本表单
     */
    @GetMapping("/basic")
    public String basic()
    {
        return prefix + "/basic";
    }

    /**
     * 卡片列表
     */
    @GetMapping("/cards")
    public String cards()
    {
        return prefix + "/cards";
    }

    /**
     * summernote 富文本编辑器
     */
    @GetMapping("/summernote")
    public String summernote()
    {
        return prefix + "/summernote";
    }

    /**
     * 搜索自动补全
     */
    @GetMapping("/autocomplete")
    public String autocomplete()
    {
        return prefix + "/autocomplete";
    }

    /**
     * 获取用户数据
     */
    @GetMapping("/userModel")
    @ResponseBody
    public AjaxResult userModel()
    {
        AjaxResult ajax = new AjaxResult();

        ajax.put("code", 200);
        ajax.put("value", users);
        return ajax;
    }

    /**
     * 获取数据集合
     */
    @GetMapping("/collection")
    @ResponseBody
    public AjaxResult collection()
    {
        String[] array = { "epl 1", "epl 2", "epl 3", "epl 4", "epl 5" };
        AjaxResult ajax = new AjaxResult();
        ajax.put("value", array);
        return ajax;
    }
}

class UserFormModel
{
    /** 用户ID */
    private int userId;

    /** 用户编号 */
    private String userCode;

    /** 用户姓名 */
    private String userName;

    /** 用户手机 */
    private String userPhone;

    public UserFormModel()
    {

    }

    public UserFormModel(int userId, String userCode, String userName, String userPhone)
    {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

}
