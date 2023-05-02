package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    //通过用户名查询用户对象
    public User queryUserByName(String uname);

    //查询所有的销售人员,查询用户放在用户模块做
    List<Map<String,Object>> queryAllSales();
}