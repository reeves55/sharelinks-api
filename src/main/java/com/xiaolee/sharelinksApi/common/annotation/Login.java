package com.xiaolee.sharelinksApi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Login {
    boolean require() default true;
    int type() default 0; // 用户类型，0=>普通用户,1=>管理员
}
