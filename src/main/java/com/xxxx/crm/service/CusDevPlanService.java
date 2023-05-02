package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 * 客户开发计划的service层
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;



    /**
     * 多条件分页查询客户开发计划(返回的数据格式必须满足LayUi中数据表格中要求的格式)
     * @param cusDevPlanQuery 客户开发机会查询对象
     * @return
     */
    public Map<String,Object> queryCusDevPlanParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String, Object> map = new HashMap<>();

        //开启分页，传入相应的页面页数，一个页面的数量
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        //得到对应的分页对象,把查询到记录总数交给分页对象处理，自动完成分页。
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());//页面总数
        //设置分页好的列表
        map.put("data",pageInfo.getList());//数据列表
        return map;
    }

    /**
     * 添加客户开发计划
     * 1.参数校验
     *      营销机会Id,非空，数据存在
     *      计划项内容 非空
     *      计划时间 非空
     * 2.设置参数的默认值
     *      是否有效  默认有效
     *      创建时间  系统当前时间
     *      修改时间  系统当前时间
     * 3.执行添加操作，判断受影响的行数
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        /*1.参数校验*/
        checkCusDevPlanParams(cusDevPlan);
        /*2.设置参数的默认值*/
        //是否有效  默认有效
        cusDevPlan.setIsValid(1);
        //创建时间  系统当前时间
        cusDevPlan.setCreateDate(new Date());
        //修改时间  系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        // 3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1,"计划项数据添加失败!");
    }

    /**
     * 更新客户开发计划
     * 1.参数校验
     *      营销机会Id,非空，数据存在
     *      营销计划Id  非空  数据存在
     *      计划项内容 非空
     *      计划时间 非空
     * 2.设置参数的默认值
     *      修改时间  系统当前时间
     * 3.执行更新操作，判断受影响的行数
     * 这个更新和添加的重复代码很多，可以考虑封装一下，二合一
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        //1.参数校验
        //营销计划Id  非空(null判断)  数据存在（数据库查询）
        AssertUtil.isTrue(null == cusDevPlan.getId() || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常，请重试");
        checkCusDevPlanParams(cusDevPlan);
        //2.更新修改时间
        cusDevPlan.setUpdateDate(new Date());
        //3.执行更新操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项更新失败");

    }






    /**
     * 参数校验
     * 1.参数校验
     *      销机会Id,非空，数据存在
     *      计划项内容 非空
     *      计划时间 非空
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会ID  非空，数据存在
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(null == sId || saleChanceMapper.selectByPrimaryKey(sId) == null,"数据异常,请重试");
        //计划项内容 非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划内容不能为空");
        //计划时间 非空
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(),"计划时间不能为空");
    }


    /**
     * 删除计划项
     *  1.判断Id是否为空，且数据存在
     *  2.修改isValid属性,并且可以更改更新时间
     *  3.执行更新操作
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id) {
        //1.判断Id是否为空，且数据存在
        AssertUtil.isTrue(null == id,"待删除记录不存在");

        //通过Id查询计划项对象,且数据存在
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(cusDevPlan == null  ,"待删除记录不存在");

        //2.修改isValid属性,通过数据库中查询出来的原对象，进行改变属性，再进行更新即可
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());

        //3.执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项删除失败");
    }
}
