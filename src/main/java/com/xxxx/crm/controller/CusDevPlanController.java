package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.BaseQuery;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户开发计划
 * @author 鲁海晶
 * @version 1.0
 */

@RequestMapping("/cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    //注入营销机会service对象进行方法调用
    @Resource
    private SaleChanceService saleChanceService;
    //注入营销开发计划service对象进行方法调用
    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 进入客户开发计划的页面
     * @return
     */
   @RequestMapping("/index")
   public String index(){
       return "/cusDevPlan/cus_dev_plan";
   }

    /**
     * 打开计划项开发与详情页面
     * @param id
     * @return
     */
   @RequestMapping("/toCusDevPlanPage")
   public String toCusDevPlanPage(Integer id, HttpServletRequest request){
       //通过id查询营销机会对象
       SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将对象设置到请求域中
       request.setAttribute("saleChance",saleChance);


       return "cusDevPlan/cus_dev_plan_data";
   }

    /**
     *客户开发计划数据查询(分页多条件查询)
     * 如果flag的值不为空，且值为1，则表示当前登录用户，并且是已分配状态的。,并且查询的是客户开发计划;否则 查询营销机会数据
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        //调用service层方法返回map集合
        return cusDevPlanService.queryCusDevPlanParams(cusDevPlanQuery);
    }

    /**
     * 添加营销机会的相应计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功!");
    }

    /**
     * 进行添加或修改计划项的页面
     * @return
     */
    @RequestMapping("/toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId,Integer id, HttpServletRequest request){
        //将营销机会的Id设置到请求域中，给计划项页面获取
        request.setAttribute("sid",sId);
        //通过计划项Id查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "/cusDevPlan/add_update";
    }


    /**
     * 更新营销机会的相应计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功!");
    }

    /**
     * 删除营销机会的相应计划项，(实际上是更新操作，父类没有相应的方法)
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项删除成功!");
    }



}
