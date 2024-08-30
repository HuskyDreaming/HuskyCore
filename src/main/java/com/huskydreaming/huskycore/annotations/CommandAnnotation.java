package com.huskydreaming.huskycore.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface CommandAnnotation {

    String label();
    String[] arguments() default "";
}