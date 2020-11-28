package org.noir.guice.boot.annotations;

import java.lang.annotation.*;

/**
 * Injectable annotation
 * 
 * 标示可注入提供executor进行识别并交由IoC管理
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {

    String named() default "";

}
