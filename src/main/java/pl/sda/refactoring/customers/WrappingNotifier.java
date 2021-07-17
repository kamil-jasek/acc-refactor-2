package pl.sda.refactoring.customers;

import static java.util.Objects.requireNonNull;

abstract class WrappingNotifier implements Notifier {

    private final Notifier nextNotifier;

    protected WrappingNotifier(Notifier nextNotifier) {
        this.nextNotifier = requireNonNull(nextNotifier);
    }

    @Override
    public void notify(CustomerRegistrationNotification notification) {
        this.nextNotifier.notify(notification);
    }
}
