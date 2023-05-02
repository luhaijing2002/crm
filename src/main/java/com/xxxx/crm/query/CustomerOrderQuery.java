package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author 鲁海晶
 * @version 1.0
 * 定义一个查询分页对象
 */
public class CustomerOrderQuery extends BaseQuery {

    private String cusId;//客户id

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }
}
