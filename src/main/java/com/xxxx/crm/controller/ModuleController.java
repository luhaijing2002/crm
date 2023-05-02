package com.xxxx.crm.controller;

import com.xxxx.crm.annoation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.vo.Module;
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
 */
@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    /**
     * 查询所有的资源列表
     * @return
     */
    @RequestMapping("/queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 进入授权页面
     * @param roleId
     * @return
     */
    @RequestMapping("/toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        //将需要授权的角色ID设置到请求域中
        request.setAttribute("roleId",roleId);
        return "/role/grant";
    }

    /**
     * 查询所有的资源记录，全字段
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 进入资源页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "/module/module";
    }



    /**
     * 添加资源
     * @param module
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("资源添加成功");
    }



    /**
     * 修改资源
     * @param module
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("资源更新成功");
    }


    /**
     * 删除资源
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModuleById(id);
        return success("资源删除成功");
    }



    /**
     * 打开添加资源的页面
     *
     * @param grade 层级
     * @param parentId 父id
     * @return
     */
    @RequestMapping("/toAddModulePage")
    public String toAddModulePage(Integer grade,Integer parentId,HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "/module/add";
    }


    @RequestMapping("/toUpdateModulePage")
    public String toUpdateModulePage(Integer id,HttpServletRequest request){
        //将要修改的资源对象设置到请求域中
        Module module = moduleService.selectByPrimaryKey(id);
        request.setAttribute("module",module);
        return "/module/update";
    }


}
