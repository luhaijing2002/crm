package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.OrderDetailsMapper;
import com.xxxx.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {

    @Resource
    private OrderDetailsService orderDetailsService;
}
