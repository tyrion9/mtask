package com.github.tyrion9.mtask;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MTaskParam {
    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";
}
