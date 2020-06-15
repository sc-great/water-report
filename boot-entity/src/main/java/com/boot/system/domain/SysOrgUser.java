package com.boot.system.domain;

import com.boot.common.core.domain.BaseEntity;

public class SysOrgUser extends BaseEntity {
    private String orgId;
    private String userId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
