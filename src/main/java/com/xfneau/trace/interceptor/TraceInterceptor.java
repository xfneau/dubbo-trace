package com.xfneau.trace.interceptor;

import com.xfneau.trace.service.TraceContext;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author xfneau
 * @description:
 * @Date Created in 16:50 2018/4/10
 */
@Aspect
@Component
public final class TraceInterceptor {

    @Before("tracePointcut()")
    public void beforeInvoke() {
        TraceContext.getInstance().init();
    }

    @After("tracePointcut()")
    public void afterInvoke() {
        TraceContext.getInstance().destroy();
    }

    @Pointcut("@annotation(com.xfneau.trace.annotation.Trace)")
    private void tracePointcut() {
    }

}
