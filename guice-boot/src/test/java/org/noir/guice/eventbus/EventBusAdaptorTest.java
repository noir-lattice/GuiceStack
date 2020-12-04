package org.noir.guice.eventbus;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedBindingBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * EventBusAdaptor Test
 * @author noir
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ EventBus.class, EventBusAdaptor.class })
public class EventBusAdaptorTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private Binder binder;

    @Mock
    private AnnotatedBindingBuilder<EventBus> builder;

    private EventBusAdaptor eventBusAdaptor;

    @Before
    public void before() {
        eventBusAdaptor = new EventBusAdaptor();
    }
    
    @Test
    public void test_apply() throws Exception {
        PowerMockito.whenNew(EventBus.class).withNoArguments().thenReturn(eventBus);
        Mockito.when(binder.bind(EventBus.class)).thenReturn(builder);
        eventBusAdaptor.apply(Collections.singletonList(InnerEventBusAdaptorTest.class), binder);

        // then 
        verify(builder, only()).toInstance(eventBus);
        verify(eventBus, only()).register(InnerEventBusAdaptorTest.class);
    }

    public class InnerEventBusAdaptorTest implements EventListener<Event> {

        @Override
        public void subscript(Event event) {
            System.out.println("test sub");
        }
    
        
    }
}
