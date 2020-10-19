package com.tmxbase.fileserverdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"license-config.properties"}) //加载额外的配置
public class FileServerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServerDemoApplication.class, args);
    }

}
