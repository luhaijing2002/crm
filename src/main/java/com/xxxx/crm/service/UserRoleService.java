package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 鲁海晶
 * @version 1.0
 * 用户角色服务
 */
@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;
}
