package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.vo.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;


    /**
     *
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        //得到对应的分页对象
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置好分页的列表
        map.put("data",pageInfo.getList());
        return map;
    }


}
