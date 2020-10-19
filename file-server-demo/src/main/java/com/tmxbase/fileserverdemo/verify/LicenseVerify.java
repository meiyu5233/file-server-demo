package com.tmxbase.fileserverdemo.verify;

import com.tmxbase.fileserverdemo.model.CustomKeyStoreParam;
import com.tmxbase.fileserverdemo.model.LicenseVerifyParam;
import de.schlichtherle.license.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * @Auther meiyu
 * @Date 2020/9/25
 */
@Component
public class LicenseVerify {

    private static final Logger logger = LoggerFactory.getLogger(LicenseVerify.class);

    public static boolean IS_INSTALL = false;

    @Autowired
    private LicenseInstall install;

    /**
     * 安装证书
     * @param param
     * @return
     */
    public synchronized LicenseContent install(LicenseVerifyParam param){
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //1. 安装证书
        try{
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();
            result = licenseManager.install(new File(param.getLicensePath()));
            logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",format.format(result.getNotBefore()),format.format(result.getNotAfter())));
            IS_INSTALL = true;
        }catch (Exception e){
            logger.error("证书安装失败！",e);
        }

        return result;
    }


    /**
     * 校验证书
     * @return
     */
    public boolean verify() {
        if(IS_INSTALL == false){
            install.licenseStartInstall();
        }
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",format.format(licenseContent.getNotBefore()),format.format(licenseContent.getNotAfter())));
            return true;
        }catch (Exception e){
            logger.error("证书校验失败！",e);
            return false;
        }
    }


    /**
     * 初始化参数
     * @param param
     * @return
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param){
        Preferences preferences = Preferences.userRoot().node("/com/tmxbase/verify");

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                ,param.getPublicKeysStorePath()
                ,param.getPublicAlias()
                ,param.getStorePass()
                ,null);

        return new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,publicStoreParam
                ,cipherParam);
    }

}
