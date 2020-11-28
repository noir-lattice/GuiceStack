package org.noir.guice.boot.scanner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ClassScannerTest {
    private static final String JAR_PACKAGE = "org.junit";
    private static final String FILE_PACKAGE = "org.noir.guice.boot";

    private FileScanner fileScanner;
    private JarScanner jarScanner;

    @Before
    public void init() {
        fileScanner = FileScanner.create(null);
        jarScanner = JarScanner.create();
    }

    @Test
    public void search_can_search_class_with_file() {
        Set<Class<?>> search = fileScanner.search(FILE_PACKAGE);
        assertFalse(search.isEmpty());
    }

    @Test
    public void search_can_search_class_with_file_and_predicate() {
        Set<Class<?>> search =
                fileScanner.search(FILE_PACKAGE, clazz -> clazz.equals(ClassScannerTest.class));
        assertFalse(search.isEmpty());
    }

    @Test
    public void search_can_search_class_with_jars() {
        Set<Class<?>> search = jarScanner.search(JAR_PACKAGE);
        assertFalse(search.isEmpty());
    }

    @Test
    public void search_can_search_class_with_jars_and_predicate() {
        Set<Class<?>> search = jarScanner.search(JAR_PACKAGE, clazz -> clazz.equals(Assert.class));
        assertFalse(search.isEmpty());
    }

}