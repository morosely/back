package com.efuture.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAutoConfiguration
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.efuture.common.*.receive(..))")
    public void log(){}

    @AfterThrowing(pointcut = "log()", throwing = "ex")
    public void exceptionLog(Throwable ex) throws Throwable {
        ex.printStackTrace();
        throw ex;
    }

    @Around(value = "log()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result;

        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    /*private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBean<?> result = new ResultBean();

        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setCode(ResultBean.FAIL);
        } else {
            logger.error(pjp.getSignature() + " error ", e);

            result.setMsg(e.toString());
            result.setCode(ResultBean.FAIL);

            // 未知异常是应该重点关注的，这里可以做其他操作，如通知邮件，单独写到某个文件等等。
        }

        return result;
    }*/
}
