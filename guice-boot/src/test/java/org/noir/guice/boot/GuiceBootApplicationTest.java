package org.noir.guice.boot;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noir.guice.boot.annotations.Bootstrap;
import org.noir.guice.boot.context.ApplicationContext;
import org.noir.guice.boot.context.GracefullyCloseContext;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ GuiceBootApplication.class, ApplicationContext.class })
public class GuiceBootApplicationTest {

    String[] packages;
    
    @Before
    public void before() throws Exception {
        PowerMockito.mockStatic(ApplicationContext.class, GracefullyCloseContext.class);
        PowerMockito.doNothing().when(ApplicationContext.class, "setScanPackage", any());
        PowerMockito.doReturn(true).when(ApplicationContext.class, "refresh");
        packages = new String[] {};
    }

    @Test
    public void can_run_with_class_has_annotation() throws Exception {
        GuiceBootApplication.run(A.class);
    }

    @Test
    public void can_fail_when_class_not_annotation() {
        Exception ex = null;
        try {
            GuiceBootApplication.run(B.class);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
    }

    @Test
    public void can_run_with_class_default_package() {
        GuiceBootApplication.run(C.class);
    }

    @Bootstrap(scanPackage = "org.noir")
    public class A {}

    public class B {}

    @Bootstrap()
    public class C {}
}
