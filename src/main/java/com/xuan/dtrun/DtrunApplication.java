package com.xuan.dtrun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xuan.dtrun.mapper")
public class DtrunApplication {

    public static void main(String[] args) {
        SpringApplication.run(DtrunApplication.class, args);
    }

}
