package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 拦截需要自动填充的函数
     *
     * @param
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 拦截需要自动填充的函数，进行参数的赋值
     *
     * @param
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始公共字段填充");
        //获得被拦截方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获得被拦截方法的注解
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        //获得被拦截方法的注解的值
        OperationType value = annotation.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        Object entity = args[0];

        //根据不同的操作类型，执行反射来赋值
        if (value == OperationType.INSERT){
            try {

                //为四个参数赋值
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
                setCreateUser.invoke(entity, currentId);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if (value == OperationType.UPDATE){
            //为两个参数赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }


}
