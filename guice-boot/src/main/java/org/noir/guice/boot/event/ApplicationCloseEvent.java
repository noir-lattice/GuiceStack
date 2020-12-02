package org.noir.guice.boot.event;

import org.noir.guice.eventbus.Event;

/**
 * Application close event
 *
 * 应用关闭事件
 */
public class ApplicationCloseEvent extends Event {

    public static ApplicationCloseEvent of(String cause) {
        return new ApplicationCloseEvent(cause);
    }

    private final String cause;

    private ApplicationCloseEvent(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    public static final ApplicationCloseEvent START_FAIL = ApplicationCloseEvent.of("Boot starting failed");
}
