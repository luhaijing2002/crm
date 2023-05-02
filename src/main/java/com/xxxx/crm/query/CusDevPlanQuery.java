package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author 鲁海晶
 * @version 1.0
 * 定义一个客户开发查询的对象,继承父类的基本查询对象，用于方便做分页，这个对象传递专门用于分页的。
 */
public class CusDevPlanQuery extends BaseQuery {

    private Integer saleChanceId;//营销机会的主键

    //营销机会的主键，来在客户开发计划表中查询对应的计划。
    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
