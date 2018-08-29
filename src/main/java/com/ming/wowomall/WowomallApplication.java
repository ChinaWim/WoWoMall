package com.ming.wowomall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

import java.lang.reflect.Proxy;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
@MapperScan("com.ming.wowomall.dao")
public class WowomallApplication {
    public static void main(String[] args) {
        SpringApplication.run(WowomallApplication.class, args);
    }
}
