package org.noir.guice.boot.executor;

import org.noir.guice.boot.scanner.ClassScanner;
import org.noir.guice.boot.scanner.FileScanner;
import org.noir.guice.boot.scanner.JarScanner;

import java.util.Set;
import java.util.function.Predicate;

public class ScanExecutor implements ClassScanner {

    private volatile static ScanExecutor instance;

    private ScanExecutor() {
    }

    @Override
    public Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate) {
        ClassScanner fileSc = new FileScanner();
        Set<Class<?>> fileSearch = fileSc.search(packageName, predicate);
        ClassScanner jarScanner = new JarScanner();
        Set<Class<?>> jarSearch = jarScanner.search(packageName, predicate);
        fileSearch.addAll(jarSearch);
        return fileSearch;
    }

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
