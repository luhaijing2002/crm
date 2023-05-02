package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 鲁海晶
 * @version 1.0
 * 模块服务
 */
@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的资源列表
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        //查询所有的资源列表
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        //查询指定角色已经授权过的资源列表，(查询角色拥有的资源id)
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsRoleId(roleId);
        //判断角色是否拥有资源ID
        if(permissionIds != null && permissionIds.size() > 0){
            //循环所有的资源列表，判断用户的资源id中是否有匹配，如果有，则设置checked属性为true
            treeModelList.forEach(treeModel -> {
                //判断角色拥有的资源ID中是滞有当前遍历的资源ID
                if(permissionIds.contains(treeModel.getId())){
                    //如果包含，则说明角色授权过，设置checked为true
                    treeModel.setChecked(true);
                }
            });
        }


        return treeModelList;
    }

    public Map<String,Object> queryModuleList(){
        Map<String,Object> map = new HashMap<>();
        //查询资源列表
        List<Module> moduleList = moduleMapper.queryModuleList();

        //不需要进行分页,但是要满足treeTable的数据返回格式与数据表格一样
        map.put("code",0);
        map.put("msg","");
        map.put("count",moduleList.size());//存放集合的元素的个数
        map.put("data",moduleList);//存放查询到的数据集合
        return map;
    }

    /**
     * 添加资源
     *  1.参数校验
     *      层级 grade
     *          非空，0|1|2
     *      模块名称 moduleName
     *          非空，同一层级下模块名称唯一
     *
     *
     *      地址url
     *          二级菜单(grade=1),非空且同一层级不可重复
     *
     *      父级菜单 parentId
     *          一级菜单(目录 grade=0) -1
     *          二级|三级菜单(菜单|按钮 grade=1或2) 非空，父级菜单必须存在
     *

     *      权限码：optvalue
     *          非空，不可重复
     *  2.设置参数的默认值
     *      是否有效isValid 1
     *      创建时间CreateDate 系统当前时间
     *      修改时间updateDate 系统当前时间
     *
     *  3.执行添加方法
     *
     *
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        /*1.参数校验*/
        //层级 grade
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1|| grade == 2),"菜单层级不合法!");

        //模块名称 moduleName 非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        //模块名称 moduleName 在同一层级模块名称下唯一
        AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName()),"模块名称重复，请重新输入");

        //如果是二级菜单(grade=1)
        if(grade == 1){
            //地址url 二级菜单(grade=1),非空
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"URL不能为空");
            //地址url 二级菜单(grade=1) 且同一层级下不可重复
            AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl()),"url已存在");
        }
        //父级菜单 parentId
        //一级菜单(目录 grade=0) -1
        if(grade == 0){
            module.setParentId(-1);
        }
        //二级|三级菜单(菜单|按钮 grade=1或2) 非空，父级菜单必须存在
        if(grade != 0){
            //非空
            AssertUtil.isTrue(module.getParentId() == null,"父级菜单不能为空");
            //父级菜单必须存在(将父级菜单的Id作为主键，查询资源记录)
            //select * from t_module where id = #{parentId}
            AssertUtil.isTrue(null == moduleMapper.selectByPrimaryKey(module.getParentId()),"请指定父级菜单");
        }
        //权限码：optvalue 非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()) , "权限码不能为空");
        //权限码：optvalue 不可重复,查到就是错
        AssertUtil.isTrue(null != moduleMapper.queryModuleByOptValue(module.getOptValue()) ,"权限码已存在!");
        /*2.设置参数的默认值*/
        //是否有效isValid 1
        module.setIsValid((byte) 1);
        //创建时间CreateDate 系统当前时间
        module.setCreateDate(new Date());
        //修改时间updateDate 系统当前时间
        module.setUpdateDate(new Date());
        /*3.执行添加操作*/
        AssertUtil.isTrue(moduleMapper.insertSelective(module) != 1,"执行添加失败");

    }


    /**
     * 修改资源(和添加相比，会多传一个参数：资源id)
     *  1.参数校验
     *          非空，数据存在
     *      层级：grade
     *          非空 0|1|2
     *      模块名称 moduleName
     *          非空，同一层级下模块名称唯一(不包含当前修改记录本身)
     *      地址url
     *          二级菜单(grade=1)非空且同一层级下不可重复(不包含当前修改记录本身)
     *      权限码 optValue
     *          非空，不可重复，(不包含当前修改记录本身)
     *
     * 2.设置参数的默认值
     *      修改时间updateDate 系统当前时间
     * 3.执行更新操作，判断受影响的行数
     *
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        //1.参数校验(非空，数据存在)
        AssertUtil.isTrue(module.getId() == null,"资源id不能为空");
        AssertUtil.isTrue(moduleMapper.selectByPrimaryKey(module.getId()) == null,"待更新记录不存在");

        //层级：grade 非空 0|1|2
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2 ),"菜单层级不合法!");

        //模块名称 moduleName     非空,同一层级(grade)下模块名称唯一(不包含)
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        //通过层级与模块名称查询资源对象(不包含当前修改记录本身))
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        if(temp != null) {
            AssertUtil.isTrue(!temp.getId().equals(module.getId()), "该层级下菜单名已经存在!");
        }

        //地址url 二级菜单(grade = 1),非空且同一层级下不可重复(不包含当前修改记录本身)
        if(grade == 1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"菜单URL不能为空");
            //通过层级与菜单URL查询资源对象
            temp = moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            //判断是否存在
            if(temp != null ){
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该层级下菜单URL已存在!");
            }
        }

        //权限码 optValue      非空，不可重复(不包含当前修改记录本身)
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if(temp != null){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"权限码已存在!");

        }

        /*2.设置参数的默认值 -修改时间updateDate 系统当前时间*/
        module.setUpdateDate(new Date());

        /*3.执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) != 1,"资源更新失败!");
    }

    /**
     * 删除资源
     *     1.判断删除的记录是否存在
     *     2.如果当前资源存在子记录，则不可删除
     *     3.删除资源时，将对应权限表的记录也删除(判断权限表中是否存在关联数据,如果存在，则删除)
     *     4.执行删除(更新)操作，判断受影响的行数
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModuleById(Integer id) {
        Module temp = moduleMapper.selectByPrimaryKey(id);
        //1判断id是否为空，并且是否存在
        AssertUtil.isTrue(null == id || temp == null,"待删除资源不存在");

        //2如果当前资源存在子记录(将id当做父id查询资源记录)
        Integer count = moduleMapper.queryModuleByParentId(id);
        //如果存在子记录，则不可删除
        AssertUtil.isTrue(count > 0,"当前资源存在子记录，不可删除!");

        //3.删除资源时，将对应权限表的记录也删除(判断权限表中是否存在关联数据,如果存在，则删除)
        count = permissionMapper.countPermissionByModuleId(id);
        //判断是否存在，存在则删除
        if(count > 0){
            //删除指定资源ID的权限记录
            permissionMapper.deletePermissionByModuleId(id);
        }

        //设置记录无效，进行删除
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());

        //执行更新
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp) != 1,"删除资源失败");

    }
}
