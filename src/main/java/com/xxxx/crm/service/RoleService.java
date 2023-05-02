package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 鲁海晶
 * @version 1.0
 * 角色service
 */
@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;

    //注入权限mapper
    @Resource
    private PermissionMapper permissionMapper;


    //注入资源mapper
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色列表
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /***
     * 多条件查询角色，并进行分页
     * @return
     */
    public Map<String, Object> toRolePage(RoleQuery roleQuery) {


        //这是一个固定的写法可以进行封装方法，类都可以。
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        //2.得到对应的分页对象,把查询到记录总数交给分页对象处理，自动完成分页。得到一个分页信息对象,里面存放数据信息，有分页信息
        PageInfo<Role> pageInfo = new PageInfo<>(roleMapper.queryParameters(roleQuery));

        //3.封装成要返回的数据表格的格式
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());//页面总数
        map.put("data",pageInfo.getList());//查询出来的分页的相关数据

        return map;
    }

    /**
     * 1.添加用户角色
     *    1.判断角色名称    唯一
     *    2.判断角色备注
     * 2.设置用户角色的默认值
     *     设置is_valia()
     *     设置角色创建时间
     *     设置角色的更新时间
     *  3.调用方法进行添加
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        //1.判断参数
        checkParameter(role.getRoleName(), role.getRoleRemark(),null);
        //2.设置用户角色的默认值(补全属性值)
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //3.调用方法进行添加
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1,"用户角色添加失败");
    }

    /**
     * 校验参数
     *      角色名称   非空，不重复
     *          先通过用户名进行查询，数据库有没有对应的记录，如果有记录，还进行与自己的id进行比较。两个id不同,或为id为空不一致说明，角色名被使用了。
     *          判断用户姓名是唯一，如果通过用户名查询出来不为空，可能会重名，再进行判断这个名字id与自己传过来的id是否相等，如果不相等就重名了，
     *      角色备注   不能为空
     * @param roleName
     * @param roleRemark
     */
    private void checkParameter(String roleName, String roleRemark,Integer id) {
        //1.判断用户姓名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(roleName),"角色名称不能为空");
        //2.判断用户姓名是唯一，如果通过用户名查询出来不为空，可能会重名，再进行判断这个名字id与自己传过来的id是否相等，如果不相等就重名了，
        Role role =  roleMapper.queryRoleBy(roleName);
        AssertUtil.isTrue(role != null && !(role.getId().equals(id)),"该角色已经存在，请重新输入");
        //3.判断用户备注非空
        AssertUtil.isTrue(roleRemark == null,"角色备注不能为空");
    }


    /**
     * 更新角色
     *  1.判断id是否为空,并且判断数据库中没有id对应的角色记录
     *  2.参数校验
     *  3.设置默认值
     *      更新时间
     *  4.调用方法进行更新
     *
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        //1.对id进行判断是否为空，并且判断数据库中没有id对应的角色记录
        AssertUtil.isTrue(role.getId() == null || roleMapper.selectByPrimaryKey(role.getId()) == null  ,"待更新角色记录不存在!");
        //2.参数校验
        checkParameter(role.getRoleName(), role.getRoleRemark(),role.getId());
        //3.设置默认值

        role.setUpdateDate(new Date());
        //4.调用方法进行更新
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1,"用户更新失败");
    }


    /**
     * 删除单个角色
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer id) {
        AssertUtil.isTrue(id == null || roleMapper.selectByPrimaryKey(id) == null,"待删除记录不存在");
        AssertUtil.isTrue(roleMapper.deleteRoleById(id) != 1 ,"角色删除失败");
    }


    /**
     * 角色授权是对权限表的操作，所以要生成权限表相关的文件
     *
     * 将对应的角色id与资源Id,添加到对应的权限表中
     *      直接添加权限：不合适，会出现重复的权限数据(执行修改权限操作后删除权限操作时)
     *      推荐使用
     *          先将已有的权限记录删除，再将需要的设置的权限记录添加
     *          1,通过角色ID查询对应的权限记录
     *          2。如果权限记录存在，则删除对应的角色拥有的权限记录
     *          3.如果有权限记录，则添加权限记录
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        //1.通过角色id查房对应的权限记录。
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        //2.如果权限记录存在，则删除对应的角色拥有的权限记录
        if(count > 0 ){
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        //3.如果传过来权限id，则添加权限记录
        if(mIds != null && mIds.length > 0){
            //定义Permission集合
            List<Permission> permissionList = new ArrayList<>();
            //设置默认值，保存到集合中,添加时没有主键
            for (Integer mId: mIds) {
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mId);
                //资源表的模块对应的授权码
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //将对象设置到集合中
                permissionList.add(permission);
            }
            //执行批量添加操作，判断受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList) != permissionList.size(),"角色授权失败");
        }

    }
}
