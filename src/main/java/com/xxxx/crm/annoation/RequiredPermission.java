package com.xxxx.crm.annoation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * @author 鲁海晶
 * @version 1.0
 * 权限注解
 * 定义方法需要的对应资源的权限码
 */
//添加相关的元注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    //权限码
    String code() default "";

}
