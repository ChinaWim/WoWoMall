package com.ming.wowomall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.reflect.Proxy;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
@EnableScheduling
@MapperScan("com.ming.wowomall.dao")

public class WowomallApplication extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(WowomallApplication.class, args);
    }
//    打包成war包
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WowomallApplication.class);
    }

}
