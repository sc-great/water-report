package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂填报日期登记对象 ReportDateInfo
 * 
 * @author EPL
 * @date 2020-03-26
 */
public class ReportDateInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 填报日期 */
    private String reportDate;

    /** 水厂编号 */
    private String factoryId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryId() {
        return factoryId;
    }
}
