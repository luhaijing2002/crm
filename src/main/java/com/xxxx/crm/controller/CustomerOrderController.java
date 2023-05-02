package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.query.CustomerOrderQuery;
import com.xxxx.crm.service.CustomerOrderService;
import com.xxxx.crm.vo.CustomerOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 * 订单控制层
 */
@Controller
@RequestMapping("/order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderService customerOrderService;


    /**
     * 通过传过来的客户id,去查询客户对应的订单记录
     * @param customerOrderQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryOrderListByCusId(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryOrderListByCusId(customerOrderQuery);
    }

    /**
     *  打开订单详情的页面
     * @param orderId
     * @return
     */
    @RequestMapping("/orderDetailPage")
    public String orderDetailPage(Integer orderId, HttpServletRequest request){
        //通过订单id进行查询订单的详情,在客户订单接口中，的主键id是orderId订单Id
        CustomerOrder customerOrder = customerOrderService.selectByPrimaryKey(orderId);
        //存放到隐藏域里面去
        request.setAttribute("order",customerOrder);
        return "/customer/customer_order_detail";
    }


}
