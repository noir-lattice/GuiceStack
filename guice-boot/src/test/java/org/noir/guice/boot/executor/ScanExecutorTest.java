package org.noir.guice.boot.executor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.noir.guice.boot.scanner.FileScanner;
import org.noir.guice.boot.scanner.JarScanner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileScanner.class, JarScanner.class})
public class ScanExecutorTest {

    private ScanExecutor instance;

    @Mock
    private FileScanner fileScanner;

    @Mock
    private JarScanner jarScanner;

    @Before
    public void before() {
        PowerMockito.mockStatic(FileScanner.class);
        PowerMockito.mockStatic(JarScanner.class);
        Mockito.when(FileScanner.create(null)).thenReturn(fileScanner);
        Mockito.when(JarScanner.create()).thenReturn(jarScanner);
        Set<Class<?>> clazzSet = new HashSet<>();
        clazzSet.add(FileScanner.class);
        Mockito.when(fileScanner.search("test.package", null)).thenReturn(clazzSet);
        clazzSet = new HashSet<>();
        clazzSet.add(JarScanner.class);
        Mockito.when(jarScanner.search("test.package", null)).thenReturn(clazzSet);
        instance = ScanExecutor.getInstance();
    }

    @Test
    public void search() {
        Set<Class<?>> search = instance.search("test.package");
        assertTrue(search.contains(FileScanner.class));
        assertTrue(search.contains(JarScanner.class));
    }
}