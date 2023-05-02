package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.service.CustomerService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;


    /**
     *进行多条件查询，
     * @param customerQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryByParamsForTable(customerQuery);
    }


    /**
     * 进入到客户信息管理的信息页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "/customer/customer";
    }

}
