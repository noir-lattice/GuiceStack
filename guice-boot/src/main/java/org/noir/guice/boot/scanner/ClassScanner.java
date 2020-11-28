package org.noir.guice.boot.scanner;

import java.util.Set;
import java.util.function.Predicate;

public interface ClassScanner {

    String CLASS_SUFFIX = ".class";

    /**
     * 扫描过滤获取类集合
     *
     * @param packageName 包名
     * @param predicate   过滤条件
     * @return 类对象集合
     */
    Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate);

    default Set<Class<?>> search(String packageName) {
        return search(packageName, null);
    }

}
