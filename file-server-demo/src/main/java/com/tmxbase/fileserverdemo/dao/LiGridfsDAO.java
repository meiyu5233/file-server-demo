package com.tmxbase.fileserverdemo.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther meiyu
 * @Date 2020/10/13
 */
@Repository
public class LiGridfsDAO {

    private static final Logger logger = LoggerFactory.getLogger(LiGridfsDAO.class);

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    //获取所有license文件名
    public Map<Object, String> getFileName() {
        Map<Object, String> resultMap = new HashMap<>();
        GridFsResource[] licenseFiles = gridFsTemplate.getResources("*");
        for (GridFsResource liceseFile : licenseFiles) {
            resultMap.put(liceseFile.getId(), liceseFile.getFilename());
        }
        return resultMap;
    }

    //通过文件名下载license文件
    public void getFileByName(String filename) throws Exception {
        ObjectId id = null;
        GridFsResource[] licenseFiles = gridFsTemplate.getResources("*");
        for (GridFsResource liceseFile : licenseFiles) {
            if(liceseFile.getFilename().equals(filename)){
                id = (ObjectId) liceseFile.getFileId();
            }
        }
        File localfile = new File("src/main/resources/License/"+filename);
        if(id==null){
            logger.error("证书不存在");
        }
        Query query = Query.query(Criteria.where("_id").is(id));
        GridFSFile file = gridFsTemplate.findOne(query);
        if (null != file) {
            GridFSDownloadStream in = gridFSBucket.openDownloadStream(file.getFilename());
            GridFsResource resource = new GridFsResource(file, in);
            InputStream inputStream = resource.getInputStream();
            byte[] f = getBytes(inputStream);
//            response.setHeader("Content-Disposition", "inline;fileName=\"" + new String((file.getFilename()).getBytes("utf-8"), "ISO8859-1") + "\"");
            OutputStream ops = new FileOutputStream(localfile);
//            OutputStream out = response.getOutputStream();
            ops.write(f);
//            out.write(f);
        }
    }

    //通过objectId获取license文件
    public void getFileByID(HttpServletResponse response,String id) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(id));
        GridFSFile file = gridFsTemplate.findOne(query);
        if (null != file) {
            GridFSDownloadStream in = gridFSBucket.openDownloadStream(file.getFilename());
            GridFsResource resource = new GridFsResource(file, in);
            InputStream inputStream = resource.getInputStream();
            byte[] f = getBytes(inputStream);
            response.setHeader("Content-Disposition", "inline;fileName=\"" + new String((file.getFilename()).getBytes("utf-8"), "ISO8859-1") + "\"");
            OutputStream out = response.getOutputStream();
            out.write(f);
        }
    }

    private byte[] getBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int i = 0;
        while (-1 != (i = inputStream.read(b))) {
            bos.write(b, 0, i);
        }
        return bos.toByteArray();
    }
}
