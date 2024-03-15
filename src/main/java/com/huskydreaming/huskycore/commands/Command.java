package com.huskydreaming.huskycore.commands;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface Command {
    String label();

    String arguments() default "";

    String[] aliases() default {};

    boolean debug() default false;
}