package pl.sda.refactoring.customers.event;

public interface EventListener {

    void process(Event event);

    boolean supports(String type);
}
