package com.xxxx.crm.aspect;

import com.xxxx.crm.annoation.RequiredPermission;
import com.xxxx.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 鲁海晶
 * @version 1.0
 * 定义一个切面
 */
@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    /**
     * 切面会拦截指定包下的指定注解
     *
      * @param pjp
     * @return
     *
     * Aspect:切面
     * Around：环绕
     * contains：包含
     *
     * 通过aop切面在方式的周围加上一个权限判定，
     * 在执行该方法时前，进行切面的工作，对权限进行一个判定，如果包含不在权限的范围(在session中的权限集合对象)内，
     * 则进行进行异常的抛出，可以由全局异常进行处理。
     *
     *
     */
    @Around(value = "@annotation(com.xxxx.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        //得到当前登录拥有的所有权限
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        //判断用户是否拥有权限(session作用域)
        if(null == permissions || permissions.size() < 1){
            //抛出认证异常
            throw new AuthException();
        }
        //得到对应的目标（MethodSignature）方法签名(返回类型与方法名来组成)，目标方法
        MethodSignature methodSignatures = (MethodSignature) pjp.getSignature();
        //得到方法上的注解
        RequiredPermission requiredPermissionAnnotation = methodSignatures.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //判断注解上对应的状态码
        if(!(permissions.contains(requiredPermissionAnnotation.code()))){
            //如果权限中不包含当前方法上注解指定的权限码，则抛出异常
            throw new AuthException();
        }
        //规定了有调用这个方法前做的通知叫前置通知。
        //如果想使用环绕通知，必须要显示的调用proceed()这个方法
        result = pjp.proceed();//目标方法的调用
        return result;//方法执行的结果。
    }

}
