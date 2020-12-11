package org.noir.guice.boot.annotations;

import java.lang.annotation.*;

/**
 * Bootstrap annotation
 * 
 * 标示应用启动类并告知应用所在的包路径
 * @see org.noir.guice.boot.GuiceBootApplication
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bootstrap {

    /**
     * 默认的将在启动类所在的包下进行扫描
     */
    String[] scanPackage() default {};

}
