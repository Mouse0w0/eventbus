package com.github.mouse0w0.eventbus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EventBusTest {

    private static EventBus eventBus;
    private static ExampleListener listener;
    private static ExampleGenericListener genericListener;

    @BeforeAll
    public static void init() {
        eventBus = new SimpleEventBus();
        listener = new ExampleListener();
        eventBus.register(listener);

        genericListener = new ExampleGenericListener();
        eventBus.register(genericListener);
    }

    @Test
    public void test() {
        ExampleEvent event = new ExampleEvent();
        eventBus.post(event);
        eventBus.unregister(listener);
        eventBus.post(new ExampleGenericEvent<>(String.class));
        Assertions.assertTrue(event.isCancelled());
        Assertions.assertTrue(genericListener.normalTestDone);
        Assertions.assertTrue(genericListener.genericTestDone);
    }
}
