package com.boot.framework.web.service;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.CommonUtils;
import com.boot.common.utils.DateUtils;
import org.springframework.stereotype.Service;

/**
 * 工具类
 * */
@Service("toolsUtils")
public class ToolsUtilsService {
    /**
     * 随机数
     * */
    public String random(){
        return CommonUtils.randomNumber(6);
    }
    /**
     * 随机数
     * */
    public String random(int len){
        return CommonUtils.randomNumber(len);
    }
    /**
     * 版本号
     * */
    public String version(){
        return "1.1.2";
    }

    /**
     * 时间字符串和当前日期比较大小
     * */
    public int compareTimeByNow(String dateTimeStr){
        String nowDateTime= DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss");
        return dateTimeStr.compareTo(nowDateTime);
    }
}
