package com.tmxbase.fileserverdemo.web;

import com.tmxbase.fileserverdemo.dao.LiGridfsDAO;
import com.tmxbase.fileserverdemo.service.HelloProcess;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Auther meiyu
 * @Date 2020/10/12
 */
@RestController
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private HelloProcess helloProcess;

    @Autowired
    private LiGridfsDAO ligridfsDAO;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 获取保存的所有证书名
     * @return
     */
    @RequestMapping("/getAllFileName")
    public Map<Object,String> getFileName() {
        return ligridfsDAO.getFileName();
    }

    /**
     * 将证书存放到mongodb中
     * @param
     * @throws FileNotFoundException
     */
    @RequestMapping("/filePush")
    public void filePush(@RequestParam(value = "filename",required = true) String filename) throws FileNotFoundException {
        File file = new File(licensePath+filename);
        FileInputStream fio = new FileInputStream(file);
        ObjectId objectId = gridFsTemplate.store(fio,file.getName(), StandardCharsets.UTF_8);
        System.out.println("文件保存ID：" + objectId);
    }


    @RequestMapping("/hello")
    public String say(){
         return helloProcess.sayHello();
    }
}
