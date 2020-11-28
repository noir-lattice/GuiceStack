package org.noir.guice.boot.executor;

import org.noir.guice.boot.scanner.ClassScanner;
import org.noir.guice.boot.scanner.FileScanner;
import org.noir.guice.boot.scanner.JarScanner;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Scan executor
 *
 * 类扫描执行器，会通过构建JarScanner与FileScanner实现扫描
 * @see FileScanner
 * @see JarScanner
 */
public class ScanExecutor implements ClassScanner {

    private volatile static ScanExecutor instance;

    private final ClassScanner fileSc = FileScanner.create(null);
    private final ClassScanner jarScanner = JarScanner.create();

    private ScanExecutor() {
    }

    @Override
    public Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate) {
        Set<Class<?>> fileSearch = fileSc.search(packageName, predicate);
        Set<Class<?>> jarSearch = jarScanner.search(packageName, predicate);
        fileSearch.addAll(jarSearch);
        return fileSearch;
    }

    /**
     * Double check instance
     */
    public static ScanExecutor getInstance() {
        if (instance == null) {
            synchronized (ScanExecutor.class) {
                if (instance == null) {
                    instance = new ScanExecutor();
                }
            }
        }
        return instance;
    }

}
