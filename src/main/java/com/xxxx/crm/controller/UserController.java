package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Controller
//@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;


    /**
     * 用户登录
     * 返回的ResultInfo对象变成JSON字符串，由ajax请求的回调函数的参数来获取。
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResultInfo userLogin( String userName,String userPwd){


        ResultInfo resultInfo =  new ResultInfo();


        //调用service层的登录方法
        UserModel userModel = userService.userLogin(userName, userPwd);

        //设置ResultInfo的result的值(将数据返回给请求)，如果想要拿到数据库中查询到的信息，需要在service层中时返回该对象。
        //拿到该对象后，要定义一个ajax请求需要的数据的用户信息，把无用信息排除掉。没有必要把所有的信息返回
        resultInfo.setResult(userModel);

//        //通过try catch捕获service层的异常，如果service层抛出异常,则说明登陆失败。否则登陆成功
//        try {
//            //调用service层的登录方法
//            UserModel userModel = userService.userLogin(userName, userPwd);
//
//            //设置ResultInfo的result的值(将数据返回给请求)，如果想要拿到数据库中查询到的信息，需要在service层中时返回该对象。
//            //拿到该对象后，要定义一个ajax请求需要的数据的用户信息，把无用信息排除掉。没有必要把所有的信息返回
//            resultInfo.setResult(userModel);
//
//        } catch (ParamsException p) {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//        } catch (Exception e){
//            //如果有其它的异常，就要进行设置一下，如果不设置最后会显示在控制台上,为服务器异常
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登陆失败");
//        }
        return resultInfo;
    }


    /**
     * 修改密码的功能
     * @param request 请求对象
     * @param sourcePwd 源密码
     * @param newPwd    新密码
     * @param confirmPwd 确认密码
     * @return
     */
    @PostMapping("/updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String sourcePwd, String newPwd, String confirmPwd){
        ResultInfo resultInfo = new ResultInfo();

        //用户id前台没有进行传递，则需要获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPassword(userId, sourcePwd, newPwd, confirmPwd);

//        try {
//            //用户id前台没有进行传递，则需要获取cookie中的userId
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            userService.updateUserPassword(userId, sourcePwd, newPwd, confirmPwd);
//
//        } catch (ParamsException p) {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("用户密码修改失败");
//        }
        return resultInfo;
    }


    /**
     * 进入修改密码的页面。
     * @return
     */
    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("/queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }


    /**
     * 分页多条件查询用户列表
     * @param userQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        /*父接口中定义好的，方法用来多条件查询用户列表，并返回map*/
        return userService.queryByParamsForTable(userQuery);
    }


    /**
     * 进入用户列表页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return  "/user/user";
    }

    /**
     * 用户添加
     * @param user
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功");
    }

    /**
     * 用户更新
     * @param user
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功");
    }



    /**
     * 进入添加/修改用户数据页面
     * @return
     */
    @RequestMapping("/toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest request){
        //判断用户Id,通过id是否为空
        if(id != null){
            //通过id查询营销机会数据
            User updateUser = userService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("updateUser",updateUser);
        }
        return "/user/add_update";
    }


    /**
     * 删除用户
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);
        return success("用户删除成功");
    }


}
