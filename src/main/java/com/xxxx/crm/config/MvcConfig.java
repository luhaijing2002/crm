package com.xxxx.crm.config;

import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.interceptor.NoLoginInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author 鲁海晶
 * @version 1.0
 * 定义一个拦截器的配置类，继承mvc配置器适配器
 * 重写一个添加拦截器的方法，并添加相应拦截器，设置相应的资源拦截和放行相应资源
 * 2.将
 */
@Configuration//配置类
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean //将拦截器方法方法返回值交给IOC维护
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现了拦截器功能的实例的对象，这是使用是noLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                //设置需要被拦截的资源
                .addPathPatterns("/**")//默认拦所有资源
                //设置不需要被拦截的资源，静态资源，还有像index首页要放行，还有登录资源也要放行
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**","/index","/user/login");//放行静态资源

    }
}
