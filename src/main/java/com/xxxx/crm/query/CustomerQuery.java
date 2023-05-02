package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @author 鲁海晶
 * @version 1.0
 * 多条件查询和分页查询时使用的查询对象
 */

public class CustomerQuery extends BaseQuery {
    private String customerName;//客户名称
    private String customerNo;//客户编号
    private String level;//客户级别


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
