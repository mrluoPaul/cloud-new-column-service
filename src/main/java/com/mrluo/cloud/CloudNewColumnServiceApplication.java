package com.mrluo.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan({"com.mrluo.cloud.modules.app.mapper"})
public class CloudNewColumnServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNewColumnServiceApplication.class, args);
    }

}