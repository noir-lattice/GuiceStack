package org.noir.guice.boot.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bootstrap {

    String scanPackage() default "";

}
