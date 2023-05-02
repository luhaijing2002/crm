package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //根据用户id查询角色记录的数量。
    Integer countUserRoleByUserId(Integer userId);
    //根据用户ID删除用户角色记录
    Integer deleteUserRoleyByUserId(Integer userId);
}