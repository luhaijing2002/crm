package com.xxxx.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

/**
 * @author 鲁海晶
 * @version 1.0
 * 启动类，整个spring 项目的启动入口
 */
@SpringBootApplication
@MapperScan("com.xxxx.crm.dao")
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}
