package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    //查询所有的角色列表(只需要id与roleName)
    public List<Map<String,Object>> queryAllRoles(Integer userId);


    //通过多参数查询所有角色列表
    public List<Role> queryParameters (RoleQuery roleQuery);

    //根据角色名来查询角色
    Role queryRoleBy(String roleName);

    Integer deleteRoleById(Integer id);
}