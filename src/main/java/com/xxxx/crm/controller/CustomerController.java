package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.service.CustomerService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Customer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        return customerService.queryCustomerByParams(customerQuery);
    }


    /**
     * 进入到客户信息管理的信息页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "/customer/customer";
    }


    /**
     * 客户添加
     * @param customer
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("客户记录添加成功!");
    }

    /**
     * 客户修改
     * @param customer
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("客户记录更新成功!");
    }




    /**
     * 进入添加|修改客户页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, HttpServletRequest request){
        if(id != null){
            Customer customer = customerService.selectByPrimaryKey(id);
            request.setAttribute("customer",customer);
        }
        return "/customer/add_update";
    }


    /**
     * 删除客户记录
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id){
        customerService.deleteCustomer(id);
        return success("客户记录删除成功!");
    }


    /**
     * 打开对应客户的订单视图，进行转发，查看对应的客户的订单情况
     * @param cid 客户id
     * @return
     */
    @RequestMapping("/orderInfoPage")
    public String orderInfoPage(Integer cid,HttpServletRequest request){
        //查询客户信息，设置到域对象中
        Customer customer = customerService.selectByPrimaryKey(cid);
        request.setAttribute("customer",customer);
        return "/customer/customer_order";
    }







}
