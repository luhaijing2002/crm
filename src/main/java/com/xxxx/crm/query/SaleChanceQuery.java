package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * 营销机会的查询类
 * @author 鲁海晶
 * @version 1.0
 */
public class SaleChanceQuery extends BaseQuery {
    //分页参数
    //条件查询


    //营销机会管理 条件查询
    private String customerName;//客户名
    private String createMan;//创建人
    private Integer state;//分配状态 0未分配 1已分配


    //客户开发计划 条件查询
    private String devResult;//开发状态
    private Integer assignMan;//指派人




    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
