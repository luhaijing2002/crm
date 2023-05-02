package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author 鲁海晶
 * @version 1.0
 * 角色多条件分页查询类
 */

public class RoleQuery extends BaseQuery {

    //角色的名字
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
