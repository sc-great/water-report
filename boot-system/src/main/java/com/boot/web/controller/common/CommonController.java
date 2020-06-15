package com.boot.web.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.boot.common.config.Global;
import com.boot.common.config.ServerConfig;
import com.boot.common.constant.Constants;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.text.Convert;
import com.boot.common.utils.CommonUtils;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.file.FileUploadUtils;
import com.boot.common.utils.file.FileUtils;
import com.boot.system.domain.SysFiles;
import com.boot.system.service.ISysFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.boot.common.utils.file.FileUploadUtils.defaultBaseDir;

/**
 * 通用请求处理
 * 
 * @author epl
 */
@Controller
@RequestMapping("/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private ISysFilesService sysFilesService;

    /**
     * 下载上传的附件文件
     * 
     * @param id 文件名称
     */
    @GetMapping("/downloadAnnex")
    public void downloadAnnex(String id, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            SysFiles sysFiles=sysFilesService.selectSysFilesById(id);
            if(sysFiles!=null){
                String fileName=sysFiles.getName();
                String path=sysFiles.getPath();
                if(path.startsWith("/")){
                    path=path.substring(1);
                }
                path=path.substring(path.indexOf("/"));
                path=defaultBaseDir+path;
                // 文件地址，真实环境是存放在数据库中的
                File file = new File(path);
                // 路径为文件且不为空则进行删除
                if (file.isFile() && file.exists())
                {
                    // 穿件输入对象
                    FileInputStream fis = new FileInputStream(file);
                    // 设置相关格式
                    response.setContentType("application/force-download");
                    // 设置下载后的文件名以及header
                    response.addHeader("Content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"));
                    // 创建输出对象
                    OutputStream os = response.getOutputStream();
                    // 常规操作
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while((len = fis.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    fis.close();
//                    if (!FileUtils.isValidFilename(fileName))
//                    {
//                        throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
//                    }
//                    String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
                    String realFileName = fileName;
                    String filePath = Global.getDownloadPath() + fileName;
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("multipart/form-data");
                    response.setHeader("Content-Disposition",
                            "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
                    FileUtils.writeBytes(filePath, response.getOutputStream());
                }else{
                    throw new Exception(StringUtils.format("未找到文件{}，不允许下载。 ", fileName));
                }
            }else{
                throw new Exception(StringUtils.format("未找到文件ID{}，不允许下载。 ", id));
            }
        }
        catch (Exception e)
        {
            //log.error("下载文件失败", e);
        }
    }
    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
//            if (!FileUtils.isValidFilename(fileName))
//            {
//                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
//            }
//            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String realFileName = fileName;
            String filePath = Global.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = Global.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // 本地资源路径
        String localPath = Global.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
    /**
     * 公示计算表
     * */
    public static void main(String[] args) throws Exception {
        String str = "a==true&&(b<20 && a==false)";
        /*str = str.replaceAll("or", "||");
        str = str.replaceAll("and", "&&");*/
        System.out.println(str);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("a",true);
        engine.put("b",10);
        engine.put("c",20);
        Object result = engine.eval(str);
        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);
        int a=Convert.toInt(Math.ceil(Convert.toDouble(2*3)/100));
        System.out.println(a);
    }
}
