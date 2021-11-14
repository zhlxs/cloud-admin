package com.it.advice;

import cn.hutool.core.util.StrUtil;
import com.it.annotation.LogAnnotation;
import com.it.entity.SysLogs;
import com.it.service.SysLogService;
import com.it.utils.SysUserUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HSL
 * @date 2021-11-13 16:22
 * @desc 统一日志处理
 */
@Aspect
@Component
public class LogAdvice {

    @Autowired
    private SysLogService sysLogService;

    @Around(value = "@annotation(com.it.annotation.LogAnnotation)")
    public Object logAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        SysLogs sysLogs = new SysLogs();
        sysLogs.setUser(SysUserUtil.getLoginUser());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String module;
        LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
        module = logAnnotation.module();
        if (StrUtil.isEmpty(module)) {
            ApiOperation apiOperation = methodSignature.getMethod().getDeclaredAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                module = apiOperation.value();
            }
        }
        if (StringUtils.isEmpty(module)) {
            throw new RuntimeException("没有指定日志module");
        }
        sysLogs.setModule(module);
        try {
            Object object = joinPoint.proceed();
            sysLogs.setFlag(true);
            return object;
        } catch (Exception e) {
            sysLogs.setFlag(false);
            sysLogs.setRemark(e.getMessage());
            throw e;
        } finally {
            if (sysLogs.getUser() != null) {
                sysLogService.save(sysLogs);
            }
        }
    }
}
