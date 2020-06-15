package com.boot.system.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 区域对象 r_area
 * 
 * @author EPL
 * @date 2020-03-23
 */
public class Area extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 区域编号 */
    private String areaId;

    /** 区域名称 */
    private String areaName;

    /** 所在组 */
    private String groupType;

    /** 排序序号 */
    private String sortNo;

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }
}
