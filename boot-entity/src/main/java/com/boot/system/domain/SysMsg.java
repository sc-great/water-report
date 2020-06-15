package com.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.boot.common.annotation.Excel;
import com.boot.common.core.domain.BaseEntity;

/**
 * 用户消息对象 sys_msg
 *
 * @author boot
 * @date 2019-12-27
 */
public class SysMsg extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 接收用户ID
     */
    @Excel(name = "接收用户ID")
    private String receiveUserId;

    /**
     * 接收用户编号
     */
    @Excel(name = "接收用户编号")
    private String receiveUserCode;

    /**
     * 接收用户名称
     */
    @Excel(name = "接收用户名称")
    private String receiveUserName;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String msgTitle;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String msgContent;

    /**
     * 消息类型
     */
    @Excel(name = "消息类型")
    private String msgType;

    /**
     * 消息状态
     */
    @Excel(name = "消息状态")
    private String msgState;

    /**
     * 最近阅读时间
     */
    @Excel(name = "最近阅读时间")
    private String readTime;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String msgParams;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public String getReceiveUserCode() {
        return receiveUserCode;
    }

    public void setReceiveUserCode(String receiveUserCode) {
        this.receiveUserCode = receiveUserCode;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getReadTime() {
        return readTime;
    }

    public String getMsgParams() {
        return msgParams;
    }

    public void setMsgParams(String msgParams) {
        this.msgParams = msgParams;
    }
}
