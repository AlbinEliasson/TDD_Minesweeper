package com.dt042g.project.mvc.observer;

import com.dt042g.project.testinghelpers.TestingConcreteObserved;
import com.dt042g.project.testinghelpers.TestingConcreteObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing unit-tests for the *Observed* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class ObservedTests {
    private static final int instanceCount = 5;

    private static Field fieldObservers;

    private final Observed[] observeds = new Observed[instanceCount];
    private final Observer[] observers = new Observer[instanceCount];

    @BeforeAll
    public static void setupAll() throws NoSuchFieldException {
        fieldObservers = Observed.class.getDeclaredField("observers");
        fieldObservers.setAccessible(true);
    }

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() {
        for(int i = 0; i < instanceCount; i++)
            observeds[i] = Mockito.spy(new TestingConcreteObserved());

        for(int i = 0; i < instanceCount; i++)
            observers[i] = Mockito.spy(new TestingConcreteObserver());
    }

    /**
     * Method for ensuring that it is possible to attach an observer object
     * to an observed object.
     */
    @Test
    public void observed_AttachObserver_OneToOne() throws IllegalAccessException {
        for(int i = 0; i < instanceCount; i++)
            observeds[i].attachObserver(observers[i]);

        for(int i = 0; i < instanceCount; i++) {
            List<Observer> attached = (List<Observer>) fieldObservers.get(observeds[i]);

            Assertions.assertTrue(attached.contains(observers[i]));
            Assertions.assertEquals(1, attached.size());
        }
    }

    /**
     * Method for ensuring that it is possible to attach multiple observers
     * to multiple observeds simultaneously.
     */
    @Test
    public void observed_AttachObserver_ManyToMany() throws IllegalAccessException {
        for(int i = 0; i < instanceCount; i++)
            for(int j = 0; j < instanceCount; j++)
                observeds[i].attachObserver(observers[j]);

        for(int i = 0; i < instanceCount; i++) {
            List<Observer> attached = (List<Observer>) fieldObservers.get(observeds[i]);

            for(int j = 0; j < instanceCount; j++) {
                Assertions.assertTrue(attached.contains(observers[i]));
            }

            Assertions.assertEquals(instanceCount, attached.size());
        }
    }

    /**
     * Method for ensuring that an observer object can't be attached multiple
     * times simultaneously to a single observed object.
     */
    @Test
    public void observed_AttachObserver_Duplicate() throws IllegalAccessException {
        for(int i = 0; i < instanceCount; i++) {
            for (int j = 0; j < instanceCount; j++) {
                observeds[i].attachObserver(observers[j]);
                observeds[i].attachObserver(observers[j]);
            }
        }

        for(int i = 0; i < instanceCount; i++) {
            List<Observer> attached = (List<Observer>) fieldObservers.get(observeds[i]);

            // Ensure that all observers are attached
            for(int j = 0; j < instanceCount; j++) {
                Assertions.assertTrue(attached.contains(observers[i]));
            }

            // Ensure that the number of observers attached is equal to the
            // actual number of observers. It being higher would mean there are
            // duplicates attached.
            Assertions.assertEquals(instanceCount, attached.size());
        }
    }

    /**
     * Method for ensuring that observers can be detached from observed objects.
     */
    @Test
    public void observed_DetachObserver() throws IllegalAccessException {
        for(int i = 0; i < instanceCount; i++)
            for(int j = 0; j < instanceCount; j++)
                observeds[i].attachObserver(observers[j]);

        for(int i = 0; i < instanceCount; i++) {
            List<Observer> attached = (List<Observer>) fieldObservers.get(observeds[i]);

            // Iterate through each observer
            for(int j = 0; j < instanceCount; j++) {
                // Check that the observer was attached to begin with
                Assertions.assertTrue(attached.contains(observers[j]));

                // Detach the observer
                observeds[i].detachObserver(observers[j]);

                // Check that the observer was detached
                Assertions.assertFalse(attached.contains(observers[j]));

                // Check that the number of observers was lowered appropriately,
                // I.E. only this observer was removed.
                Assertions.assertEquals(instanceCount - j - 1, attached.size());
            }

            // Ensure that all observers were detached
            Assertions.assertEquals(0, attached.size());
        }
    }

    /**
     * Method for ensuring that the application behaves appropriately when
     * an attempt is made to detach a unattached observer.
     */
    @Test
    public void observed_DetachObserver_Nonexistent() throws IllegalAccessException {
        for(int i = 0; i < instanceCount; i++)
            for(int j = 0; j < instanceCount; j++)
                observeds[i].attachObserver(observers[j]);

        for(int i = 0; i < instanceCount; i++) {
            List<Observer> attached = (List<Observer>) fieldObservers.get(observeds[i]);

            for(int j = 0; j < instanceCount; j++) {
                Assertions.assertTrue(attached.contains(observers[j]));

                // Attempt to detach the observer twice
                observeds[i].detachObserver(observers[j]);
                observeds[i].detachObserver(observers[j]);

                // Check that the observer was detached
                Assertions.assertFalse(attached.contains(observers[j]));

                // Check that the number of observers was lowered appropriately,
                // I.E. only this observer was removed.
                Assertions.assertEquals(instanceCount - j - 1, attached.size());
            }

            Assertions.assertEquals(0, attached.size());
        }
    }

    /**
     * Method for ensuring that attaching and pushing events from a single
     * *Observed* instance to a single *Observer* instance functions correctly.
     */
    @Test
    public void observed_PushEvent_OneToOne() {
        // Attach one observer to each observed
        for(int i = 0; i < instanceCount; i++)
            observeds[i].attachObserver(observers[i]);

        // Iterate through each observed/observer pair
        for(int i = 0; i < instanceCount; i++) {

            // Iterate X more times, to test pushing multiple events
            for(int j = 0; j < 5; j++) {
                // Calculate a unique identifier for this iterations' event
                int ident = (i * 5) + j;

                // Push the event from the observer
                observeds[i].pushEvent("TEST" + ident, Arrays.asList(new Point(ident, ident)));

                // Validate the total number of events received by the observer
                // to ensure that it is only receiving events from the observer
                // it is attached to; and is also receiving all of them.
                Mockito.verify(observers[i], Mockito.times(j + 1)).handleEvent(Mockito.anyString(), Mockito.anyList());

                // Validate the parameters provided to handle event; which
                // should be the exact same as those passed to push event.
                Mockito.verify(observers[i]).handleEvent("TEST" + ident, Arrays.asList(new Point(ident, ident)));
            }
        }
    }

    /**
     * Method for ensuring that attaching and pushing events from multiple
     * *Observed* instances to multiple *Observer* instances functions
     * correctly.
     */
    @Test
    public void observed_PushEvent_ManyToMany() {
        // Attach all observers to all observeds
        for(int i = 0; i < instanceCount; i++)
            for(int j = 0; j < instanceCount; j++)
                observeds[i].attachObserver(observers[j]);

        // Iterate through each observed instance
        for(int i = 0; i < instanceCount; i++) {
            // Push an event to with the index as a unique identifier
            observeds[i].pushEvent("TEST" + i, Arrays.asList(new Point(i, i)));

            // Iterate through each observer which is attached to the current
            // observed instance.
            for(int j = 0; j < instanceCount; j++) {
                // Validate the total number of events received by the observer
                // to ensure that it is only receiving events from the observer
                // it is attached to; and is also receiving all of them.
                Mockito.verify(observers[j], Mockito.times(i + 1)).handleEvent(Mockito.anyString(), Mockito.anyList());

                // Validate the parameters provided to handle event; which
                // should be the exact same as those passed to push event.
                Mockito.verify(observers[j]).handleEvent("TEST" + i, Arrays.asList(new Point(i, i)));
            }
        }
    }
}
