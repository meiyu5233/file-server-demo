package com.tmxbase.fileserverdemo.service;

import com.tmxbase.fileserverdemo.interceptor.License;
import org.springframework.stereotype.Service;

/**
 * @Auther meiyu
 * @Date 2020/10/13
 */
@Service("HelloProcess")
public class HelloProcess {

    @License(resource = "say",active = "print")
    public String sayHello(){
        return "hello world";
    }
}
