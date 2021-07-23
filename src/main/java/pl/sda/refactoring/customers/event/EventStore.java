package pl.sda.refactoring.customers.event;

public interface EventStore {

    void push(Event event);

    Event take();

    void commit(Event event);
}
