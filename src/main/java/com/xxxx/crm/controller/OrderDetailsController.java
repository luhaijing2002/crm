package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Controller
@RequestMapping("/OrderData")
public class OrderDetailsController extends BaseController {

    @Resource
    private OrderDetailsService orderDetailsService;
}
