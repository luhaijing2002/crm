package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Customer;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    //通过客户名称查询客户记录
    Customer queryCustomerByCustomerName(String customerName);
}