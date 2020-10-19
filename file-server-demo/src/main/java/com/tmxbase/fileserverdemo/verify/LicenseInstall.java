package com.tmxbase.fileserverdemo.verify;

import com.tmxbase.fileserverdemo.infos.AbstractServerInfos;
import com.tmxbase.fileserverdemo.infos.WindowsInfos;
import com.tmxbase.fileserverdemo.model.LicenseVerifyParam;
import com.tmxbase.fileserverdemo.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Auther meiyu
 * @Date 2020/10/12
 */
@Component
public class LicenseInstall {

    private static final Logger logger = LoggerFactory.getLogger(LicenseInstall.class);

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;

    @PostConstruct
    public void licenseStartInstall() {
//        AbstractServerInfos infos = new WindowsInfos();
//        String filename = MD5Util.encryption(infos.getServerInfos().toString());
        String filename = "eb665ed165810988cc0ddb00c4a4b871";
        if (StringUtils.isNotBlank(filename)) {
            logger.info("++++++++ 开始安装证书 ++++++++");
            LicenseVerifyParam param = new LicenseVerifyParam();
            param.setSubject(filename);
            param.setPublicAlias(publicAlias);
            param.setStorePass(storePass);
            param.setLicensePath(licensePath+filename+".lic");
            param.setPublicKeysStorePath(publicKeysStorePath);
            LicenseVerify licenseVerify = new LicenseVerify();
            //安装证书
            licenseVerify.install(param);

            logger.info("++++++++ 证书安装结束 ++++++++");
        }
    }
}
