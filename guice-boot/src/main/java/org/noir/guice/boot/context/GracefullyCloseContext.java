package org.noir.guice.boot.context;

import java.util.concurrent.locks.LockSupport;

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
    private static final Object blocker = new Object();

    protected static void start() {
        mainThread = Thread.currentThread();
        LockSupport.park(blocker);
    }

    public static void close() {
        if (mainThread != null) {
            LockSupport.unpark(mainThread);
        }
    }

    @Override
    public void subscript(ApplicationCloseEvent event) {
        close();
        logger.info("Closed Application main thread: {}", mainThread.getName());
    }
}
