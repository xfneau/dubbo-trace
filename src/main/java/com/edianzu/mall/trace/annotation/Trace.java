package com.edianzu.mall.trace.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * trace注解用于非web入口日志追踪。通过拦截器拦截添加该注解的方法。
 * @author xfneau
 * @description:
 * @Date Created in 16:53 2018/4/10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Trace {
}
