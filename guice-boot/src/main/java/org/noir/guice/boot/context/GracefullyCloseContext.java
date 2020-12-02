package org.noir.guice.boot.context;

import jdk.internal.misc.Unsafe;
import org.noir.guice.boot.event.ApplicationCloseEvent;
import org.noir.guice.boot.executor.binder.impl.BeforeBindPostProcessorBinder;
import org.noir.guice.eventbus.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gracefully close context
 *
 * 支持优雅关闭的上下文
 */
public class GracefullyCloseContext implements EventListener<ApplicationCloseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(BeforeBindPostProcessorBinder.class);

    private static Thread mainThread;
    private static final Unsafe U = Unsafe.getUnsafe();

    protected static void start() {
        mainThread = Thread.currentThread();
        U.park(false, 0L);
    }

    public static void close() {
        if (mainThread != null) {
            U.unpark(mainThread);
        }
    }

    @Override
    public void subscript(ApplicationCloseEvent event) {
        close();
        logger.info("Closed Application main thread: {}", mainThread.getName());
    }
}
