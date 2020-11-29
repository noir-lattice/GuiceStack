package org.noir.guice.boot.context;

import jdk.internal.misc.Unsafe;

public class GracefullyCloseContext {
    private static Thread mainThread;
    private static final Unsafe U = Unsafe.getUnsafe();

    protected static void start() {
        mainThread = Thread.currentThread();
        U.park(false, 0L);
    }

    public static void close() {
        U.unpark(mainThread);
    }
}
