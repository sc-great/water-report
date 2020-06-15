package com.boot.common.enums;

/**
 * 用户状态
 * 
 * @author epl
 */
public enum UserStatus
{
    OK("1", "正常"), DISABLE("0", "停用"), DELETED("1", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
