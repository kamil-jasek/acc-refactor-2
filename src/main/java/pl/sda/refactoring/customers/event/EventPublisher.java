package pl.sda.refactoring.customers.event;

public interface EventPublisher {

    void notifyListeners(Event event);

    void registerListener(EventListener eventListener);

    void unregisterListener(EventListener eventListener);
}
