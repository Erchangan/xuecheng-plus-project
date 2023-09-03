package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 路由转发，校验令牌的合法性，白名单的维护
 * 一些接口是不要登录就能进行访问，比如课程预览，需要放入白名单中
 */
@SpringBootApplication
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
    }
}
