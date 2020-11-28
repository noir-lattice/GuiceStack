package org.noir.guice.boot.scanner;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Class scanner
 *
 * 类扫描器，包括jar包和文件夹下扫描
 * @see FileScanner
 * @see JarScanner
 */
public interface ClassScanner {

    String CLASS_SUFFIX = ".class";

    String DEFAULT_CLASSPATH = ClassScanner.class.getResource("/").getPath();

    /**
     * 扫描过滤获取类集合
     *
     * @param packageName 包名
     * @param predicate   过滤条件
     * @return 类对象集合
     */
    Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate);

    /**
     * 扫描过滤获取类集合
     *
     * @param packageName 包名
     * @return 类对象集合
     */
    default Set<Class<?>> search(String packageName) {
        return search(packageName, null);
    }

}
