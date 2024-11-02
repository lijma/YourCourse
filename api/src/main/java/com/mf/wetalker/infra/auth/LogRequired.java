package com.mf.wetalker.infra.auth;


import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRequired {

    boolean enable() default true;

    Role role() default Role.USER;
    
}
