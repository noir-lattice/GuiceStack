package org.noir.guice.boot.context;

import java.util.concurrent.locks.LockSupport;

/**
 * Gracefully close context
 *
 * 支持优雅关闭的上下文
 */
public class GracefullyCloseContext {

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

    public static Thread getMainThread() {
        return mainThread;
    }

}
