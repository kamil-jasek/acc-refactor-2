package pl.sda.refactoring.customers.event;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class SyncEventPublisher implements EventPublisher {

    private final EventStore eventStore;
    private final List<EventListener> listeners;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    SyncEventPublisher(EventStore eventStore) {
        this.eventStore = requireNonNull(eventStore);
        this.listeners = new ArrayList<>();
        executorService.submit(this::processInternally);
    }

    private void processInternally() {
        Event event;
        while ((event = eventStore.take()) != null) {
            process(event);
            eventStore.commit(event);
        }
    }

    private void process(Event event) {
        for (final var listener: listeners) {
            if (listener.supports(event.getType())) {
                tryProcessEvent(event, listener);
            }
        }
    }

    private void tryProcessEvent(Event event, EventListener listener) {
        try {
            listener.process(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyListeners(Event event) {
        eventStore.push(event);
    }

    @Override
    public void registerListener(EventListener eventListener) {
        if (!listeners.contains(eventListener)) {
            listeners.add(eventListener);
        }
    }

    @Override
    public void unregisterListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }
}
