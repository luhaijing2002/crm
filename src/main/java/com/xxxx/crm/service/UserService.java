package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 鲁海晶
 * @version 1.0
 */
@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 用户登陆操作
     * 1.参数判断，判断用户姓名、用户密码非空
     *      如果参数为空，抛出异常（异常被控制层捕获并处理】
     * 2,调用数据访问层，通过用户名查询用户记录，返回用户对象
     * 3,判断用户对象是否为空
     *      如果对象为空，抛出异常（异常被控制层捕获并处理
     * 4。判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
     *      如果密码不相等，抛出异常（异常被控制层捕获并处理）
     * 5,如果密码正确，登录成功
     *
     * @param userName
     * @param password
     */
    public UserModel userLogin(String userName,String password){
        /*参数判断*/
        checkLoginParams(userName,password);
        /*调用dao层，实际查询用户记录，返回用户对象*/
        User user = userMapper.queryUserByName(userName);
        /*用户对象参数是否为空*/
        AssertUtil.isTrue(user == null,"该用户不存在");
        /*判断用户是否正确,比较用户的密码是否一致，注意密码不能直接比较，因为数据库中的密码加密了*/
        checkUserPwd(password,user.getUserPwd());
        /*密码正确,则应该准备一个对象将数据进行封装起来，返回会控制层，由控制层返回响应，前端再进行数据的展示*/
        //返回构建用户模型对象，里面只有需要返回的属性
        return buildUserInfo(user);
    }


    /**
     * 修改密码
     * 开启事务
     *
     * 1.接收四个参数(用户ID、原始密码、新密码、确认密码)
     * 2.通过用户ID查询用户记录，返回用户对象
     * 3.参数校验
         * 待更新用户记录是否存在(用户对象是否为空)
         * 判断原始密码是否为空
         * 判断原始密码是否正确(查询的用户对象中的用户密码是否原始密码一致)
         * 判断新密码是否为空
         * 判断新密码是否与原始密码一致(不允许新密码与原始密码)
         * 判断确认密码是否为空
         * 判断确认密码是否与新密码一致
     * 4.设置用户的新密码
     *  需要将新密码通过指定算法进行加密(md5加密)
     * 5.执行更新操作，判断受影响的行数
     *
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //1.接收四个参数(用户ID、原始密码、新密码、确认密码)
    public void updateUserPassword(Integer userId,String sourcePwd,String newPwd,String confirmPwd){
        //1.通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //2.参数校验
            //待更新用户记录是否存在(用户对象是否为空)
        AssertUtil.isTrue(user == null,"待更新记录用户不存在");

        //参数校验
        checkPasswordParams(user,sourcePwd,newPwd,confirmPwd);

        //4.设置用户的新密码
            //需要将新密码通过指定算法进行加密(md5加密)
        //5.执行更新操作，判断受影响的行数,直接把查询到的用户对象进行密码属性的进行修改，放入到更新的语句中传入用户信息
        user.setUserPwd(Md5Util.encode(newPwd));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"修改密码失败!");
    }


    /**
     * 修改密码的参数校验
     *
     * @param user
     * @param sourcePwd
     * @param newPwd
     * @param confirmPwd
     */
    private void checkPasswordParams(User user, String sourcePwd, String newPwd, String confirmPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(sourcePwd),"原始密码不能为空!" );
        //判断原始密码是否正确(查询的用户对象中的用户密码是否原始密码一致),这里要注意数据库的密码是加密的,要加密后进行比较
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(sourcePwd)),"用户密码和原始密码不一致");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空!");
        //判断新密码是否与原始密码一致(不允许新密码与原始密码)
        AssertUtil.isTrue(sourcePwd.equals(newPwd),"新密码不能与原始密码相同!");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空!");
        //判断确认密码是否与新密码一致
        AssertUtil.isTrue(!confirmPwd.equals(newPwd),"确认密码和新密码必须相同");
    }


    /**
     * 构建需要返回的客户端的用户模型对象
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setUserId(user.getId());
        //设置加密的用户id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     *  先将客户端传递的密码加密，再与数据库中查询到的密码作比较。
     *
     * @param password
     * @param userPwd
     */
    private void checkUserPwd(String password, String userPwd) {
        //将客户端传递的密码加密
        password = Md5Util.encode(password);
        AssertUtil.isTrue( !(password.equals(userPwd)),"用户密码不正确");
    }

    /**
     * 参数校验的
     * @param userName
     * @param password
     */
    private void checkLoginParams(String userName, String password) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password),"用户密码不能为空");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }


    /**
     * 添加用户
     * 1.参数校验（真实姓名不是必填项）
     *      用户名userName   非空 唯一性
     *      邮箱email        非空
     *      手机号            非空，格式正确
     * 2.设置参数的默认值
     *      isValid   1
     *      createDate  创建时间
     *      updateDate  更新时间
     *      默认密码      123456->md5加密存放到数据库中
     * 3.执行添加操作
     *
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        /*1.参数校验*/
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
        /*2.设置参数的默认值*/
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码
        user.setUserPwd(Md5Util.encode("123456"));
        /*3.执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1,"用户添加失败");

        /*用户角色关联*/
        /**
         * 用户Id
         *  userId
         * 角色ID
         *  roleIds
         *
         */
        relationUserRole(user.getId(),user.getRoleIds());

    }



    /**
     * 更新用户
     * 1.参数校验（真实姓名不是必填项）
     *      判断用户ID是否为空，  且数据存在
     *
     *      用户名userName   非空 唯一性
     *      邮箱email        非空
     *      手机号           非空，格式正确
     * 2.设置参数的默认值
     *      updateDate  更新时间
     * 3.执行修改操作，判断受影响的行数
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //1.判断用户ID是否为空，  且数据存在
        AssertUtil.isTrue(user.getId() == null || userMapper.selectByPrimaryKey(user.getId()) == null,"待更新记录不存在!" );
        //2.参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());
        //3.设置参数的默认值
        user.setUpdateDate(new Date());
        //4.执行修改操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1,"用户更新失败!");

        /*用户角色关联*/
        /**
         * 用户Id
         *  userId
         * 角色ID
         *  roleIds
         *
         */
        relationUserRole(user.getId(),user.getRoleIds());
    }






    /**
     * 1.参数校验
     * 用户名userName   非空 唯一性
     * 邮箱email        非空
     * 手机号            非空，格式正确
     *
     * @param userName
     * @param email
     * @param phone
     * @param userId
     */
    private void checkUserParams(String userName, String email, String phone, Integer userId) {
        //判断用户姓名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空!");
        //判断用户名的唯一性
        //通过用户名查询用户对象
        User user = userMapper.queryUserByName(userName);
        //如果是添加操作，数据库中无数据，只要通过名称查到数据，则表示用户名被占用
        //如果是修改操作，数据库中有对应的记录，通过用户名查到数据，可能是当前记录本身，也可能是别的记录
        //如果用户名存在，且与当前修改记录不是同一个，则表示其他记录占用了该用户名，不可用。
        AssertUtil.isTrue( null != user && !(user.getId().equals(userId)),"用户名已存在,请求重新输入!");
        //邮箱email        非空
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空!");
        //手机号            非空
        AssertUtil.isTrue(StringUtils.isBlank(phone) ,"手机号不能为空!");
        //手机号 格式判断
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }

    /**
     *用户删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        //判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(ids == null || ids.length == 0,"待删除记录不存在!");


        //遍历用户的ID查询对应的用户角色记录,查询每个id有没有对应的角色，
        for (Integer userId : ids) {
            Integer count  = userRoleMapper.countUserRoleByUserId(userId);
            if( count > 0){
                //进行删除用户id对应的角色
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleyByUserId(userId) != count,"用户角色删除失败！");

            }
        }
        //执行删除操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length,"用户记录删除失败");

    }

    /**
     * 关联用户角色
     * 用户角色关联
     *     添加操作
     *     原始角色不存在
     *          1.不添加新的角色记录     不操作用户角色表
     *          2.添加新的角色记录      给指定用户绑定相关的角色记录
     * 更新操作
     *      原始角色不存在
     *          1.不添加新的角色记录     不操作用户角色表
     *          2.添加新的角色记录，     给指定用户绑定相关的角色记录
     *      原始角色存在
     *          1.添加新的角色记录     判断已有的角色信息记录不添加，添加没有的角色记录
     *          2.清空所有的角色记录     删除用户绑定角色记录
     *          3.如果移除部分角色记录    删除不存在的角色记录，存在的角色记录保留
     *          4.移除部分角色，添加新的角色   删除不存在的角色记录，存在的角色记录保留，添加新的角色
     *
     *  如何进行角色分配???(最佳解决方案。)
     *      判断用户对应的角色记录存在，先将用户原有的角色记录删除，再添加新的角色记录
     *
     * 删除操作（根据用户id删除用户-角色表的相关记录）
     * @param userId  用户ID
     * @param roleIds 角色Id
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //判断用户Id查询角色记录（查询用户有几种角色）
        Integer conut =  userRoleMapper.countUserRoleByUserId(userId);
        //判断角色记录是否存在
        if(conut > 0){
            //如果角色记录存在就要删除该角色对应的角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleyByUserId(userId) != conut ,"用户角色删除失败");
        }
        //判断角色id组是否存在，如果存在，则添加该用户对应的角色记录
        if(!StringUtils.isBlank(roleIds)){
            //将用户角色数据设置到集合中，执行批量添加，由于传过来是一个字符串，要进行转换成数组
            List<UserRole> userRoleList = new ArrayList<>();
            //将角色Id字符串转换成数组
            String[] roleIdsArray = roleIds.split(",");
            //遍历数组，得到对应的用户角色对象,填充要修改的字段，进行填充到实体类中
            for (String roleId : roleIdsArray) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.valueOf(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            //批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(),"用户角色关联失败!");


        }


    }

























}
