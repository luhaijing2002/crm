package com.xxxx.crm.controller;

import com.github.pagehelper.Page;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 * 角色控制层
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有的角色列表
     * @return
     */
    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles (userId);
    }


    /**
     * 进入用户管理视图
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "/role/role";
    }


    /**
     * 把数据表格的数据，按格式进行返回,返回给数据表格
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> toRolePage(RoleQuery roleQuery){
        return roleService.toRolePage(roleQuery);
    }


    /**
     * 跳转到别一个添加或修改的相关视图中
     * @return
     */
    @RequestMapping("/toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request){
        if(id != null){
            Role role = roleService.selectByPrimaryKey(id);
            request.setAttribute("role",role);
        }
        return "/role/add_update";
    }

    /**
     * 添加角色
     * @return
     * @param role
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("用户添加成功");
    }

    /**
     * 更新角色
     * @return
     * @param role
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.updateRole(role);
        return success("用户更新成功");
    }

    /**
     * 删除角色单个
     * @param id
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色删除成功");
    }


    /**
     * 角色授权
     * @param roleId
     * @param mIds
     * @return
     */
    @PostMapping("/addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success("权限绑定成功");
    }




}
