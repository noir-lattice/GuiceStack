package org.noir.guice.eventbus;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EventPublisher.class })
public class EventPublisherTest {
    
    @Mock
    private EventBus eventBus;

    @Mock
    private Event event;

    private EventPublisher publisher;

    @Before
    public void before() {
        publisher = new EventPublisher();
        Whitebox.setInternalState(publisher, "eventBus", eventBus);
    }

    @Test
    public void test_publish_event() {
        publisher.publishEvent(event);
        // then
        verify(eventBus, only()).post(event);
    }

}
