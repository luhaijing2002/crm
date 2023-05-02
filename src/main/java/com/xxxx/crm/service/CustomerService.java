package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
     * 查询所有的客户记录进行多条件查询，或分页显示
     * @param customerQuery
     * @return
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


    /**
     * 添加客户
     * 1.参数校验
     *      客户名称 name
     *          非空  名称唯一
     *      法人代码  fr
     *          非空
     *      手机号码 phone
     *          非空，格式正确
     * 2.设置参数的默认值
     *      设置为有效
     *      设置为正常客户(流失状态)
     *      设置创建时间
     *      设置更新时间
     *      客户编号
     *          系统生成，唯一(uuid | 时间戳 | 年月日时分秒 : 雪花算法 )
     *          格式：KH + 时间戳
     * 3.执行添加操作，判断受影响的行数
     *
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        /*1.参数校验*/
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //判断用户名是否唯一,通过用户名去查询数据库
        Customer temp =  customerMapper.queryCustomerByCustomerName(customer.getName());
        AssertUtil.isTrue(temp != null,"客户名称已存在");

        /*2.设置参数的默认值*/
        customer.setIsValid(1);
        customer.setState(0);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        //设置客户编号(唯一，由系统自动生成)
        customer.setKhno("KH" + System.currentTimeMillis());

        /*3.执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.insertSelective(customer) != 1,"客户添加失败");
    }

    /**
     * 1.参数校验
     *      id
     *          非空，且存在
     *      客户名称 name
     *          非空  名称唯一
     *      法人代码  fr
     *          非空
     *      手机号码 phone
     *          非空，格式正确
     * 2.设置参数的默认值
     *      设置更新时间
     * 3.执行更新操作，判断受影响的行数
     *
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer) {
        //1.判断id不为空，且存在(为空，或不存在)
        AssertUtil.isTrue(customer.getId() == null || customerMapper.selectByPrimaryKey(customer.getId()) == null,"待更新记录不存在");
        //2.参数校验
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //用户名要唯一
        Customer temp = customerMapper.queryCustomerByCustomerName(customer.getName());
        //如果是修改的情况下，必须要查询出来记录，并且要将自己排除后再进行，还存在，说明重名
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(customer.getId())),"客户名已存在,请重新输入!" );

        /*2.设置参数的默认值*/
        customer.setUpdateDate(new Date());

        /*3.执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) != 1,"更新客户信息失败!");
    }


    /**
     * 用于参数的校验
     *  客户名称 name
     *  法人代码  fr
     *  手机号码 phone
     * @param name
     * @param fr
     * @param phone
     */
    private void checkCustomerParams(String name, String fr, String phone) {
        //对客户名进行一个进行一个非空判断
        AssertUtil.isTrue(StringUtils.isBlank(name),"用户名称不能为空");
        //对法人进行一个参数进行判断
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人不能为空");
        //对手机号码进行一个判断
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空");
        //对手机号码进行一个校验
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正常");
    }


    /**
     * 删除客户记录
     *  1.参数校验
     *      id
     *          非空，数据存在
     *  2.设置参数默认值
     *      isValid = 0
     *      updateDate 系统当前时间
     *  3.执行删除(更新操作)判断受影响的行数
     *
     * @param id
     */
    public void deleteCustomer(Integer id) {
        /*1.参数校验（id）*/
        AssertUtil.isTrue(id == null ,"待删除记录不存在");
        //提取出来，方便设置默认值,通过id查询客户记录
        Customer customer = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customer == null ,"待删除记录不存在");

        /*2.设置默认值*/
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());

        //执行删除(更新)操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) != 1,"删除客户记录失败");
    }
}
