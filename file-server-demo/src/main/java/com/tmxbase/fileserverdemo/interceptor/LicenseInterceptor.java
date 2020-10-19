package com.tmxbase.fileserverdemo.interceptor;

import com.tmxbase.fileserverdemo.verify.LicenseVerify;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Auther meiyu
 * @Date 2020/10/12
 */
@Aspect
@Component
public class LicenseInterceptor {

    @Autowired
    private LicenseVerify licenseVerify;

    @Pointcut("execution(public * com.tmxbase.fileserverdemo.service.*.*(..))")
    public void gate() {
    }

    @Around("gate()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        //判断方法上是否有License注解
        if (targetMethod.isAnnotationPresent(License.class)) {

            //校验证书是否有效
            boolean verifyResult = licenseVerify.verify();

            if (verifyResult != true) {
                return "您的证书无效，请核查服务器是否取得授权或重新申请证书！";
            }
        }

        return pjp.proceed();
    }


}
