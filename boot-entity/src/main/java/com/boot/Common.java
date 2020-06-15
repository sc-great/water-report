package com.boot;

import com.boot.system.domain.SysFiles;
import com.boot.system.service.ISysFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import static com.boot.common.utils.file.FileUploadUtils.defaultBaseDir;
/**
 * 公共方法
 *
 * @author EPL
 * */
@Service
public class Common {
    @Autowired
    private ISysFilesService sysFilesService;

    /**
     * 获取一个文件流
     * @param id rule String 文件编号
     * */
    public FileInputStream getFileStream(String id){
        FileInputStream inputStream=null;
        SysFiles sysFiles=sysFilesService.selectSysFilesById(id);
        if(sysFiles!=null){
            String path=sysFiles.getPath();
            if(path.startsWith("/")){
                path=path.substring(1);
            }
            path=path.substring(path.indexOf("/"));
            String filePath=defaultBaseDir+path;
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    inputStream = new FileInputStream(file);
                }catch (Exception e){}
            }
        }
        return inputStream;
    }

    /**
    * Map转对象
    * @param map rule Map<String, Object>
    * @param beanClass reule Class<?>
    * */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }

    /**
    * 对象转Map
    * @param obj rule Object
    * */
    public static Map<?, ?> objectToMap(Object obj) {
        if(obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}