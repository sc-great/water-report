package com.boot.web.controller.common;

import com.boot.common.constant.Constants;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.file.FileUploadUtils;
import com.boot.system.domain.SysFiles;
import com.boot.system.service.ISysFilesService;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/**
 * 上传文件信息Controller
 *
 * @author boot
 * @date 2019-10-12
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
    @Autowired
    private ISysFilesService sysFilesService;
    private String prefix = "admin/file";

    /**
     * 文件上传
     * */
    @ResponseBody
    @RequestMapping("/doUpload")
    public AjaxResult doUpload(MultipartFile uploadfile){
        AjaxResult ajaxResult;
        try {
            if (!uploadfile.isEmpty()){
                String filePath = FileUploadUtils.upload(uploadfile);
                String fileName=uploadfile.getOriginalFilename();
                long fileSize=uploadfile.getSize();
                String type="object";
                String suffix=fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                switch (suffix){
                    case ".jpg":
                        type="image";
                        break;
                    case ".jpeg":
                        type="image";
                        break;
                    case ".gif":
                        type="image";
                        break;
                    case ".png":
                        type="image";
                        break;
                }
                SysFiles sysFiles=new SysFiles();
                sysFiles.setType(type);
                sysFiles.setName(fileName);
                sysFiles.setSize(fileSize);
                sysFiles.setPath(filePath);
                sysFiles.setSuffix(suffix);
                if(sysFilesService.insertSysFiles(sysFiles)>0){
                    ajaxResult=success("文件上传成功！",sysFiles.getId());
                }else{
                    if(FileUploadUtils.delete(filePath)){
                        ajaxResult=error("文件保存失败！");
                    }else{
                        ajaxResult=error("文件删除失败！");
                    }
                }
            }else{
                ajaxResult=error("没有上传文件！");
            }
        }catch (Exception e){
            ajaxResult=error(e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 删除文件
     * */
    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult doDelete(@RequestParam("id") String id){
        AjaxResult ajaxResult;
        try {
            SysFiles sysFiles=sysFilesService.selectSysFilesById(id);
            if(sysFiles!=null){
                String path=sysFiles.getPath();
                if(sysFilesService.deleteSysFilesByIds(id)>0){
                    FileUploadUtils.delete(path);
                    ajaxResult=success("文件删除成功！",id);
                }else{
                    ajaxResult=error("文件删除失败！");
                }
            }else{
                ajaxResult=error("未找到文件！");
            }
        }catch (Exception e){
            ajaxResult=error();
        }
        return ajaxResult;
    }

    /**
     * 打开文件
     * */
    @RequestMapping("/openFile/{id}")
    public String openFile(@PathVariable String id, ModelMap mmap){
        String url="";
        SysFiles sysFiles=sysFilesService.selectSysFilesById(id);
        if(sysFiles!=null){
            mmap.put("file",sysFiles);
            if("object".equals(sysFiles.getType())){
                if(".pdf".equals(sysFiles.getSuffix())){
                    url=prefix+"/pdf";
                }
            }else{
                url=prefix+"/img";
            }
        }
        return url;
    }

    /**
     * 获取文件信息
     * */
    @RequestMapping("/get/{id}")
    @ResponseBody
    public AjaxResult getById(@PathVariable String id) {
        AjaxResult ajaxResult;
        SysFiles sysFiles = sysFilesService.selectSysFilesById(id);
        if (StringUtils.isNotNull(sysFiles)) {
            ajaxResult = new AjaxResult(AjaxResult.Type.SUCCESS,"获取成功",sysFiles);
        }else {
            ajaxResult = new AjaxResult(AjaxResult.Type.ERROR,"获取失败");
        }
        return ajaxResult;
    }

    /**
     * 在线生产word文件
     * */
    @ResponseBody
    @RequestMapping("/doCreateWord")
    public AjaxResult doCreateWord(@RequestParam("fileName") String fileName,@RequestParam("content") String content) throws Exception {
        AjaxResult ajaxResult;
        ByteArrayInputStream bais=null;
        FileOutputStream ostream=null;
        try {
            //文件保存地址
            String path =FileUploadUtils.getDefaultBaseDir()+ "/"+ DateUtils.datePath() + "/";
            // 生成临时文件名称
            fileName=fileName+".doc";
            String temp_fileName =new Date().getTime()+".doc";
            String filePath=path+temp_fileName;
            // 检查目录是否存在
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            StringBuilder html=new StringBuilder();
            html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\t" +
                    "<HTML>\t" +
                    "<HEAD>\t" +
                    "<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=utf-8\">\t" +
                    "<TITLE></TITLE>\t" +
                    "<META NAME=\"GENERATOR\" CONTENT=\"OpenOffice 4.1.7  (Win32)\">\t" +
                    "<META NAME=\"AUTHOR\" CONTENT=\"Windows 用户\">\t" +
                    "<META NAME=\"CREATED\" CONTENT=\"20200114;1520000\">\t" +
                    "<META NAME=\"CHANGEDBY\" CONTENT=\"Windows 用户\">\t" +
                    "<META NAME=\"CHANGED\" CONTENT=\"20200114;3070000\">\t" +
                    "<META NAME=\"AppVersion\" CONTENT=\"15.0000\">\t" +
                    "<META NAME=\"DocSecurity\" CONTENT=\"0\">\t" +
                    "<META NAME=\"HyperlinksChanged\" CONTENT=\"false\">\t" +
                    "<META NAME=\"LinksUpToDate\" CONTENT=\"false\">\t" +
                    "<META NAME=\"ScaleCrop\" CONTENT=\"false\">\t" +
                    "<META NAME=\"ShareDoc\" CONTENT=\"false\">\t" +
                    "<STYLE TYPE=\"text/css\">\t" +
                    "<!--\t@page { margin-left: 3.18cm; margin-right: 3.18cm; margin-top: 2.54cm; margin-bottom: 2.54cm }\t" +
                    "P { margin-bottom: 0.21cm; direction: ltr; color: #000000; text-align: justify; widows: 0; orphans: 0 }\t-->\t" +
                    "</STYLE>" +
                    "</HEAD>" +
                    "<BODY LANG=\"zh-CN\" TEXT=\"#000000\" DIR=\"LTR\">"+content+"</BODY>" +
                    "</HTML>");
            byte b[] = html.toString().getBytes("UTF-8");
            bais = new ByteArrayInputStream(b);
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
            ostream = new FileOutputStream(filePath);
            poifs.writeFilesystem(ostream);

            int dirLastIndex = FileUploadUtils.getDefaultBaseDir().lastIndexOf("/") + 1;
            String currentDir = StringUtils.substring(FileUploadUtils.getDefaultBaseDir(), dirLastIndex);
            String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + DateUtils.datePath() + "/"+ temp_fileName;
            long fileSize=b.length;
            String type="object";
            String suffix=".doc";
            SysFiles sysFiles=new SysFiles();
            sysFiles.setType(type);
            sysFiles.setName(fileName);
            sysFiles.setSize(fileSize);
            sysFiles.setPath(pathFileName);
            sysFiles.setSuffix(suffix);
            if(sysFilesService.insertSysFiles(sysFiles)>0){
                ajaxResult=success("文件上传成功！",sysFiles.getId());
            }else{
                if(FileUploadUtils.delete(filePath)){
                    ajaxResult=error("文件保存失败！");
                }else{
                    ajaxResult=error("文件删除失败！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            ajaxResult=success("文件创建失败！");
        }finally {
            if(bais!=null){
                bais.close();
            }
            if(ostream!=null){
                ostream.close();
            }
        }
        return ajaxResult;
    }
}