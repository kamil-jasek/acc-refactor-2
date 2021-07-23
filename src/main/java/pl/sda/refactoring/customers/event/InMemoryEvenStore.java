package pl.sda.refactoring.customers.event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

final class InMemoryEvenStore implements EventStore {

    private final BlockingQueue<Event> queue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);

    @Override
    public void push(Event event) {
        queue.offer(event);
    }

    @Override
    public Event take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit(Event event) {
    }
}
