package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerOrderMapper;
import com.xxxx.crm.query.CustomerOrderQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {

    @Resource
    private CustomerOrderMapper customerOrderMapper;


    /**
     * 通过客户id去查询对应的订单记录
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryOrderListByCusId(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();
        AssertUtil.isTrue(customerOrderQuery.getCusId() == null,"待查询的客户的订单不存在");
        //开启分页
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置好分页的列表
        map.put("data",pageInfo.getList());
        return map;
    }
}
